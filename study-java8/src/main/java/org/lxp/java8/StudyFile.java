package org.lxp.java8;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StudyFile {
    public static List<String> read(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return reader.lines().collect(Collectors.toList());
        }
    }

    public static List<String> readDistinct(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return reader.lines().distinct().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        }
    }

    public static List<String> lines(String file) throws IOException {
        return Files.lines(Paths.get(file)).collect(Collectors.toList());
    }

    public static Path write(String file, byte[] bytes) throws IOException {
        return Files.write(Paths.get(file), bytes);
    }
}
