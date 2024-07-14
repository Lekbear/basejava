package com.basejava.webapp;

import java.io.File;
import java.io.PrintStream;

public class MainFile {
    public static void main(String[] args) {
        File pathFile = new File(System.getProperty("user.dir"));

        try {
            findFiles(pathFile, System.out, 0);
        } catch (Exception e) {
            throw new RuntimeException("Error", e);
        }
    }

    public static void findFiles(File file, PrintStream output, int depth) {
        if (!file.exists()) {
            return;
        }

        for (int i = 0; i < depth; i++) {
            output.print("\t");
        }

        output.println(file.getName());

        if (file.isDirectory()) {
            File[] list = file.listFiles();
            if (list != null) {
                for (File value : list) {
                    findFiles(value, output, depth + 1);
                }
            }
        }
    }
}
