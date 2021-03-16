package com.shunyi.laboratory.verification.exec;

import org.apache.commons.exec.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author Simeon Chen
 * @create 2021-02-23 9:09
 */
public class SchemaValidator {

    private String zipRootFolderName;

    public void validate(String schemaUrl, List<String> errorList) throws IOException, InterruptedException {
        String schemaExt = FilenameUtils.getExtension(schemaUrl);
        if (!StringUtils.equalsIgnoreCase(schemaExt, "zip")) {
            errorList.add("PropConfig.getProperty(\"CONF_MGT_VAL_MSG_0053\")");
        } else {
            readZip(schemaUrl, errorList);
        }
    }

    /**
     * Read zip file structure
     *
     * @param schemaUrl
     * @param errorList
     * @throws IOException
     */
    private void readZip(String schemaUrl, List<String> errorList) throws IOException, InterruptedException {
        try (FileInputStream fis = new FileInputStream(schemaUrl);
             BufferedInputStream bis = new BufferedInputStream(fis);
             ZipInputStream zis = new ZipInputStream(bis)) {
            int i = 0;
            boolean yangExist = false;
            ZipEntry ze;
            while ((ze = zis.getNextEntry()) != null) {
                if(i == 0) {
                    //Checks if root is a directory
                    if(!ze.isDirectory()) {
                        errorList.add("PropConfig.getProperty(\"CONF_MGT_VAL_MSG_0055\")");
                        return;
                    }
                    zipRootFolderName = ze.getName();
                } else if(ze.isDirectory()) {
                    //Checks if root contains any subdirectories
                    errorList.add("PropConfig.getProperty(\"CONF_MGT_VAL_MSG_0056\")");
                    return;
                } else if(FilenameUtils.getExtension(ze.getName()).equalsIgnoreCase("yang")) {
                    //Checks if yang does exist in root
                    yangExist = true;
                }
                i++;
            }
            if(!yangExist) {
                errorList.add("PropConfig.getProperty(\"CONF_MGT_VAL_MSG_0057\")");
                return;
            }
            //unzip schema file
            unzipSchema(schemaUrl, errorList);
        }
    }

    /**
     * Unzip schema file to a tmp folder
     *
     * @param schemaUrl
     * @param errorList
     * @throws IOException
     */
    private void unzipSchema(String schemaUrl, List<String> errorList) throws IOException, InterruptedException {
        File tmpDir = new File("/var/opt/ericsson/omts/tmp/ConfigMgt"+File.separator + UUID.randomUUID().toString());
        if(!tmpDir.exists()) {
            tmpDir.mkdirs();
        }
        //Unzip schema zip
        ZipUtils.unZip(schemaUrl, tmpDir);
        //Create shell.sh as main shell to execute the 'yang2dsdl ...' command
        StringBuilder shellContent = new StringBuilder();
        String destPath = tmpDir.getPath() + File.separator + zipRootFolderName;
        shellContent.append("cd \""+destPath+"\" && "+String.format(CommandUtils.COMMAND, ""));
        CommonUtils.createAndWriteTextFile(tmpDir.getPath(), "shell.sh", shellContent.toString());
        //Delete tmp directory callback after validation finished
        Callback<File, Void> callback = param -> {
            try {
                FileUtils.deleteDirectory(param);
            } catch (IOException e) {
//                System.out.println("LogBackUtil.logEventError(EVENTLOGPREFIX + PropConfig.getProperty(\"EConfigManagement-0042\"),\n" +
//                        "                                this.getClass().getName(), param.getPath(), ExceptionUtils.getFullStackTrace(e));");
            }
//            System.out.println("LogBackUtil.logEventInfo(EVENTLOGPREFIX + PropConfig.getProperty(\"EConfigManagement-0043\"),\n" +
//                    "                            this.getClass().getName(), param.getPath());");
            return null;
        };

        long s = System.currentTimeMillis();
        //Validate yang files
        validateYangFiles(errorList, tmpDir.getPath(), callback, tmpDir);
        long e = System.currentTimeMillis();
        System.out.println("validateYangFiles spent:"+(e-s)+"ms.");
    }

    /**
     * Validate yang files
     *
     * @param errorList
     * @param shellDirectory
     * @throws IOException
     */
    private void validateYangFiles(List<String> errorList, String shellDirectory, Callback<File, Void> callback, File tmpDirectory) throws IOException, InterruptedException {
        CountDownLatch c1 = new CountDownLatch(1);
        CommandLine cmdLine = CommandLine.parse("sh "+shellDirectory+File.separator+"shell.sh");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
        // create the executor and consider the exitValue '1' as success
        final Executor executor = new DefaultExecutor();
        // Watch dog is used to kills a run-away process after sixty seconds.
        ExecuteWatchdog watchdog = new ExecuteWatchdog(60000);
        executor.setWatchdog(watchdog);
        executor.setStreamHandler(streamHandler);
        executor.execute(cmdLine, new ExecuteResultHandler() {
            @Override
            public void onProcessComplete(int exitValue) {
                //The document was successfully printed ...
                System.out.println("Success("+exitValue+")");
                callback.call(tmpDirectory);
                c1.countDown();
            }

            @Override
            public void onProcessFailed(ExecuteException e) {
                System.out.println("Failed("+e.getExitValue()+")");
                if (watchdog != null && watchdog.killedProcess()) {
                    errorList.add("The validation process timed out");
                } else {
                    StringBuilder newOutput = new StringBuilder();
                    filterOutput(outputStream.toString(), newOutput);
                    if(!StringUtils.isEmpty(newOutput.toString())) {
                        errorList.add("The validation process failed to do : "+newOutput.toString());
                    }
                }
                callback.call(tmpDirectory);
                c1.countDown();
            }
        });


        boolean flag = c1.await(30, TimeUnit.SECONDS);
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
