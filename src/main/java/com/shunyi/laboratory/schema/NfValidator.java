package com.shunyi.laboratory.schema;

import org.apache.commons.exec.Executor;
import org.apache.commons.exec.*;
import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @Author: ezxenwl
 * @Date: 10/16/2020 14:29
 */
public class NfValidator {
    private int nThreads = 20;
    private long timeout = 30000;

    public NfValidator(int nThreads, long timeout) {
        this.nThreads = nThreads;
        this.timeout = timeout;
    }

    private SchemaValidator schemaValidator = new SchemaValidator();

    public void validateSchema(String dir) {
        List<String> errorList = new ArrayList<>();
        Map<String, File> unzippedSchemaPathMap = new HashMap<>();
        File file = new File(dir);
        File[] files = file.listFiles((dir1, name) -> {
            boolean matchExt = name.endsWith(".zip");
            boolean matchEricsson = name.startsWith("ericsson");
            return matchExt && matchEricsson;
        });
        for (File f : files) {
            try {
                schemaValidator.checkSchemaZipStructure(f.getName(), f.getPath(), errorList, unzippedSchemaPathMap);
            } catch (Exception e) {
                errorList.add("PropConfig.getProperty(\"CONF_MGT_VAL_MSG_0054\")");
            }
        }

        try {
            validateYangByCallingExternalCommand(files, unzippedSchemaPathMap);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a fixed thread pool
     *
     * @param nThreads
     * @return
     */
    public static ExecutorService newFixedThreadPool(int nThreads) {
        return new ThreadPoolExecutor(nThreads, nThreads, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1000), new ThreadPoolExecutor.AbortPolicy());
    }

    /**
     * Validate yang files by calling an external command
     *
     * @param unzippedSchemaPathMap
     * @throws InterruptedException
     */
    private void validateYangByCallingExternalCommand(File[] files, Map<String, File> unzippedSchemaPathMap) throws InterruptedException {
        List<String> errorList = new ArrayList<>();
        ExecutorService exService = newFixedThreadPool(nThreads);
        //Call external command in Multiple-threads
        CountDownLatch countDownLatch = new CountDownLatch(files.length);
        ///开启多线程
        for (File f : files) {
            exService.execute(() -> {
                execCommand(f.getName(), unzippedSchemaPathMap, timeout, errorList);
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        boolean flag = countDownLatch.await(files.length * timeout, TimeUnit.MILLISECONDS);
        if (flag) {
            //Do nothing. True if the count reached zero and false if the waiting time elapsed before the count reached zero
        }
        exService.shutdown();

        System.out.println("\n\n\n***************************************");
        errorList.stream().forEach(System.out::println);
        System.out.println("Back to Main thread!");
    }

    /**
     * Execute command
     *
     * @param unzippedSchemaPathMap
     * @param timeout
     */
    private void execCommand(String nf, Map<String, File> unzippedSchemaPathMap, long timeout, List<String> errorList) {
        try {
            File uuidDir = unzippedSchemaPathMap.get(nf);
            validate(timeout, uuidDir.getPath(), errorList);
            FileUtils.deleteDirectory(uuidDir);
            System.out.println("[main] delete uuid directory has finished ..." + uuidDir.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void validate(long printJobTimeout, String uuidDir, List<String> errorList) {
        ExecuteWatchdog watchdog = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);

        CommandLine commandLine = CommandLine.parse("bash " + uuidDir + File.separator+"shell.sh");
        // create the executor and consider the exitValue '0' as success
        final Executor executor = new DefaultExecutor();
        executor.setExitValue(0);
        executor.setStreamHandler(streamHandler);

        // create a watchdog if requested
        if (printJobTimeout  > 0) {
            watchdog = new ExecuteWatchdog(printJobTimeout);
            executor.setWatchdog(watchdog);
        }
        System.out.println("[print] Executing blocking print job  ...");
        try {
            executor.execute(commandLine);
        } catch (Exception e) {
            if (watchdog != null && watchdog.killedProcess()) {
                errorList.add("The validation process timed out");
            } else {
                StringBuilder newOutput = new StringBuilder();
                filterOutput(outputStream.toString(), newOutput);
                if(!newOutput.toString().equals("")) {
                    errorList.add("The validation process failed to do : "+newOutput.toString());
                }
            }
        }
    }

    private void filterOutput(String oldOutput, StringBuilder newOutput) {
        if(!oldOutput.endsWith(": fatal: Content is not allowed in prolog")) {
            String[] lines = oldOutput.split("\n");
            for(String line : lines) {
                if(line.contains(": error: ")) {
                    newOutput.append(line);
                    newOutput.append("\n");
                }
            }
        }
    }
}
