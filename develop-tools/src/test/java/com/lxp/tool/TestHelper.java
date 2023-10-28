package com.lxp.tool;

import org.junit.Assert;

import java.io.File;
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
}
