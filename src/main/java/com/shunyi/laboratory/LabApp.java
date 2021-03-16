package com.shunyi.laboratory;

import com.shunyi.laboratory.verification.exec.NodeValidator;
import com.shunyi.laboratory.verification.exec.SchemaValidator;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LabApp {

    public static void main(String[] args) {
        LabApp app = new LabApp();
        System.out.println("Input param:"+args[0]);
        app.validateSchema(args[0]);
//        exec.validateNode(args[0]);
    }

    private void validateSchema(String zipParentPath) {
        SchemaValidator schemaValidator = new SchemaValidator();
        File file = new File(zipParentPath);
        File[] files = file.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                boolean flag = name.endsWith(".zip");
                return flag;
            }
        });
        int numberOfThreads = 10;
        ExecutorService service = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        for(File f: files) {

            service.execute(() -> {
                latch.countDown();
            });

            List<String> errors = new ArrayList<>();
            try {
                schemaValidator.validate(f.getPath(), errors);
            } catch (Exception e) {
                errors.add("PropConfig.getProperty(\"CONF_MGT_VAL_MSG_0054\")");
                e.printStackTrace();
            }




            errors.stream().forEach(System.out::println);
            System.out.println("****************************\\");
        }
    }

    private void validateNode(String nodeId) {
        NodeValidator nodeValidator = new NodeValidator();
        String cdPath = "/home/omts/workspace/equxyid/omts-script/engine";
        try {
            nodeValidator.validate(nodeId, cdPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("****************************\\");
    }

}
