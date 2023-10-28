package com.lxp.tool;

import org.junit.Assert;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

public class TestHelper {
    public static void recursiveDelete(File file) {
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            for (File f : Objects.requireNonNull(file.listFiles())) {
                recursiveDelete(f);
            }
        }
        Assert.assertTrue(file.delete());
    }

    public static void copy(List<String> files, String sourceFolder, String destinationFolder) {
        File testFolderPath = new File(destinationFolder);
        Assert.assertTrue(testFolderPath.mkdir());
        files.forEach(original -> {
            try {
                Files.copy(Paths.get(sourceFolder + original), Paths.get(destinationFolder + original), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
