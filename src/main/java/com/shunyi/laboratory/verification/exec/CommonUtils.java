package com.shunyi.laboratory.verification.exec;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author esehhuc
 * @create 2021-03-10 9:32
 */
public class CommonUtils {

    /**
     * Create and write a text file
     *
     * @param destDirectory
     * @param fileName
     * @param content
     * @throws IOException
     */
    public static void createAndWriteTextFile(String destDirectory, String fileName, String content) throws IOException {
        //Write file
        Path path = Paths.get(destDirectory + File.separator + fileName);
        // default utf_8
        try (BufferedWriter bw = Files.newBufferedWriter(path)) {
            bw.write(content);
            bw.newLine();
        }
    }

}
