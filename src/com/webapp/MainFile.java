package com.webapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class MainFile {
    public static void main(String[] args) throws IOException {
        File file = new File("/Users/deadgod/IdeaProjects/basejava/test/resources/ResumeTestData.json");
        System.out.println(file.getCanonicalFile());

        File dir = new File("./src/com/webapp");
        System.out.println(dir.isDirectory());

        if (dir.isDirectory()) {
            System.out.println("Contents of the directory:");
            listFilesRecursively(dir, 0);
        }

        try (FileInputStream fis = new FileInputStream(file);
             InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8)) {
            System.out.println("Reading file:");
            int data;
            while ((data = isr.read()) != -1) {
                System.out.print((char) data);
            }
        }
    }

    private static void listFilesRecursively(File dir, int level) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                String indent = "  ".repeat(level);
                if (file.isDirectory()) {
                    System.out.println(indent + "Directory: " + file.getName());
                    listFilesRecursively(file, level + 1);
                } else {
                    System.out.println(indent +  "File: " + file.getAbsolutePath());
                }
            }
        }
    }
}
