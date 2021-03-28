package com.shunyi.laboratory.schema;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Simeon Chen
 * @create 2021-02-23 9:09
 */
public class SchemaValidator {
    private String zipRootFolderName;
    private Map<String, File> unzippedSchemaPathMap;
    private String shellName = "shell.sh";
    private String nf;
    private String schemaUrl;

    /**
     * Check the schema zip file structure
     *
     * @param errorList
     * @param unzippedSchemaPathMap
     * @throws IOException
     */
    public void checkSchemaZipStructure(String nf, String schemaUrl, List<String> errorList, Map<String, File> unzippedSchemaPathMap) {
        this.nf = nf;
        this.schemaUrl = schemaUrl;
        this.unzippedSchemaPathMap = unzippedSchemaPathMap;
        String schemaExt = FilenameUtils.getExtension(schemaUrl);
        if (!schemaExt.equalsIgnoreCase("zip")) {
            errorList.add("CONF_MGT_VAL_MSG_0053");
        } else {
            try {
                unzipAndCheck(errorList);
            } catch (Exception e) {
                errorList.add("CONF_MGT_VAL_MSG_0054");
                deleteTempDir();
            }
        }
    }

    private void unzipAndCheck(List<String> errorList) throws IOException {
        File tmpDir = new File(Paths.ConfigMgt_TMP_PATH+File.separator + UUID.randomUUID().toString());
        if(!tmpDir.exists()) {
            tmpDir.mkdirs();
        }
        //Store the temp dir used for calling external command in multiple-threads
        unzippedSchemaPathMap.put(nf, tmpDir);
        //Unzip schema zip
        ZipUtils.unZip(schemaUrl, tmpDir);

        File[] files = tmpDir.listFiles();
        if(files.length == 0) {
            errorList.add("\"CONF_MGT_VAL_MSG_0054\"");
            deleteTempDir();
            return;
        } else if(files.length > 1
                || (files.length == 1 && !files[0].isDirectory())) {
            errorList.add("\"CONF_MGT_VAL_MSG_0055\"");
            deleteTempDir();
            return;
        } else {
            File[] subDirs = files[0].listFiles((dir, name) -> new File(dir.getPath() + File.separator + name).isDirectory());
            if(subDirs.length > 0) {
                errorList.add("\"CONF_MGT_VAL_MSG_0056\"");
                deleteTempDir();
                return;
            }
            File[] yangFiles = files[0].listFiles((dir, name) -> "yang".equalsIgnoreCase(FilenameUtils.getExtension(name)));
            if(yangFiles.length < 1) {
                errorList.add("\"CONF_MGT_VAL_MSG_0057\"");
                deleteTempDir();
                return;
            }
        }
        //Get unzipped folder name
        zipRootFolderName = files[0].getName();
        generateExecShell(tmpDir);
    }

    /**
     * Create a shell with executing command
     *
     * @param tmpDir
     * @throws IOException
     */
    private void generateExecShell(File tmpDir) throws IOException {
        //Create shell.sh as main shell to execute the 'yang2dsdl ...' command
        StringBuilder shellContent = new StringBuilder();
        String destPath = tmpDir.getPath() + File.separator + zipRootFolderName;
        shellContent.append("cd \""+destPath+"\" && "+String.format(SysConstants.Commands.SCHEMA_VALIDATION_CMD, ""));
        TxtFileUtils.createAndWriteTextFile(tmpDir.getPath(), shellName, shellContent.toString());
        System.out.println("EConfigManagement-0046");
    }

    /**
     * Delete temp directory by nf name
     */
    private void deleteTempDir() {
        //Delete that temp directory named with uuid
        if(unzippedSchemaPathMap.containsKey(nf)) {
            try {
                File tmpDir = unzippedSchemaPathMap.get(nf);
                FileUtils.deleteDirectory(tmpDir);
                System.out.println("EConfigManagement-0043");
            } catch (IOException e) {
                System.out.println("EConfigManagement-0042");
            }
        }
    }
}
