package com.shunyi.laboratory.verification.exec;

import org.apache.commons.exec.*;
import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author esehhuc
 * @create 2021-03-02 9:48
 */
public class NodeValidator {

    public void validate(String nodeId, String cdPath) throws IOException {
        File tmpDir = new File("/var/opt/ericsson/omts/tmp/ConfigMgt"+File.separator + UUID.randomUUID().toString());
        if(!tmpDir.exists()) {
            tmpDir.mkdirs();
        }
        StringBuilder shellContent = new StringBuilder();
        shellContent.append("cd \""+cdPath+"\" && "+ String.format(CommandUtils.COMMAND_FOR_NODE, nodeId));
        CommonUtils.createAndWriteTextFile(tmpDir.getPath(), "node_verification.sh", shellContent.toString());
        CommandLine cmdLine = CommandLine.parse("sh "+tmpDir.getPath() + File.separator + "node_verification.sh");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
        // create the executor and consider the exitValue '0' as success
        Executor executor = new DefaultExecutor();
        executor.setExitValue(0);
        executor.setStreamHandler(streamHandler);
        executor.execute(cmdLine, new ExecuteResultHandler() {
            @Override
            public void onProcessComplete(int exitValue) {
                System.out.println("Success("+exitValue+")");
                deleteTempDir(tmpDir);
            }

            @Override
            public void onProcessFailed(ExecuteException e) {
                System.out.println("Fail("+e.getExitValue()+")");
                System.out.println("Exception:"+ e.getMessage()+"\n");
                System.out.println("Output:"+ outputStream.toString()+"\n");
                deleteTempDir(tmpDir);
            }
        });
    }

    private void deleteTempDir(File tmpDir) {
        try {
            FileUtils.deleteDirectory(tmpDir);
            System.out.println("Deleted "+tmpDir.getPath()+" successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
