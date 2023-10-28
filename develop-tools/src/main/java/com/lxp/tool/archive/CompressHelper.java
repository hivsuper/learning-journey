package com.lxp.tool.archive;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.EncryptionMethod;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

public class CompressHelper {
    public void compressSingleFile(String sourceFile, String targetFile) throws IOException {
        compressSingleFile(sourceFile, targetFile, null);
    }

    public void compressSingleFile(String sourceFile, String targetFile, String password) throws IOException {
        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setEncryptionMethod(EncryptionMethod.AES);
        File file = new File(sourceFile);
        if (Objects.isNull(password)) {// Not call #passwordProcessor intentionally
            zipParameters.setEncryptFiles(false);
            try (ZipFile zipFile = new ZipFile(targetFile)) {
                zipFile.addFile(file, zipParameters);
            }
        } else {
            zipParameters.setEncryptFiles(true);
            try (ZipFile zipFile = new ZipFile(targetFile, password.toCharArray())) {
                zipFile.addFile(file, zipParameters);
            }
        }
    }

    public void compress(String sourceFile, String targetFile, int... splitLength) throws IOException {
        compress(sourceFile, targetFile, null, splitLength);
    }

    public void compress(String sourceFile, String targetFile, String password, int... splitLength) throws IOException {
        File file = new File(sourceFile);
        if (file.isDirectory()) {
            ZipParameters zipParameters = new ZipParameters();
            zipParameters.setCompressionLevel(CompressionLevel.HIGHER);
            zipParameters.setEncryptionMethod(EncryptionMethod.AES);
            try (ZipFile zipFile = new ZipFile(targetFile, passwordProcessor.apply(zipParameters, password))) {
                if (Objects.isNull(splitLength) || splitLength.length == 0) {
                    zipFile.addFolder(file, zipParameters);
                } else {
                    zipFile.createSplitZipFileFromFolder(file, zipParameters, true, splitLength[0]);
                }
            }
        } else {
            compress(List.of(sourceFile), targetFile, password, splitLength);
        }
    }

    public void compress(List<String> sourceFiles, String targetFile, int... splitLength) throws IOException {
        compress(sourceFiles, targetFile, null, splitLength);
    }

    public void compress(List<String> sourceFiles, String targetFile, String password, int... splitLength) throws IOException {
        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setCompressionLevel(CompressionLevel.HIGHER);
        zipParameters.setEncryptionMethod(EncryptionMethod.AES);
        List<File> filesToAdd = sourceFiles.stream().map(File::new).toList();
        try (ZipFile zipFile = new ZipFile(targetFile, passwordProcessor.apply(zipParameters, password))) {
            if (Objects.isNull(splitLength) || splitLength.length == 0) {
                zipFile.addFiles(filesToAdd, zipParameters);
            } else {
                zipFile.createSplitZipFile(filesToAdd, zipParameters, true, splitLength[0]);
            }
        }
    }

    public void compressAppend(List<String> sourceFiles, String targetFile) throws IOException {
        compressAppend(sourceFiles, targetFile, null);
    }

    public void compressAppend(List<String> sourceFiles, String targetFile, String password) throws IOException {
        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setCompressionLevel(CompressionLevel.HIGHER);
        zipParameters.setEncryptionMethod(EncryptionMethod.AES);
        try (ZipFile zipFile = new ZipFile(targetFile, passwordProcessor.apply(zipParameters, password))) {
            for (String sourceFile : sourceFiles) {
                File file = new File(sourceFile);
                zipParameters.setFileNameInZip(file.getName());
                try (InputStream stream = new FileInputStream(file)) {
                    zipFile.addStream(stream, zipParameters);
                }
            }
        }
    }

    public void compressAppend(String sourceFile, String targetFile) throws IOException {
        compressAppend(sourceFile, targetFile, null);
    }

    public void compressAppend(String sourceFile, String targetFile, String password) throws IOException {
        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setCompressionLevel(CompressionLevel.HIGHER);
        zipParameters.setEncryptionMethod(EncryptionMethod.AES);
        try (ZipFile zipFile = new ZipFile(targetFile, passwordProcessor.apply(zipParameters, password))) {
            File fileToAdd = new File(sourceFile);
            if (fileToAdd.isDirectory()) {
                List<File> files = List.of(Objects.requireNonNull(fileToAdd.listFiles(file -> !file.isDirectory() && !file.isHidden())));
                zipParameters.setRootFolderNameInZip(fileToAdd.getName());
                zipFile.addFiles(files, zipParameters);

            } else {
                zipParameters.setFileNameInZip(fileToAdd.getName());
                zipFile.addFile(fileToAdd);
            }
        }
    }

    public void extract(String zipFile, String destinationDirectory) throws IOException {
        extract(zipFile, destinationDirectory, null);
    }

    public void extract(String zipFile, String destinationDirectory, String password) throws IOException {
        if (Objects.isNull(password)) {
            try (ZipFile zip = new ZipFile(zipFile)) {
                zip.extractAll(destinationDirectory);
            }
        } else {
            try (ZipFile zip = new ZipFile(zipFile, password.toCharArray())) {
                zip.extractAll(destinationDirectory);
            }
        }
    }

    public void extractSpecificFile(String zipFile, String fileToExtract, String destinationDirectory) throws IOException {
        extractSpecificFile(zipFile, fileToExtract, destinationDirectory, null);
    }

    public void extractSpecificFile(String zipFile, String fileToExtract, String destinationDirectory, String password) throws IOException {
        if (Objects.isNull(password)) {
            try (ZipFile zip = new ZipFile(zipFile)) {
                zip.extractFile(fileToExtract, destinationDirectory);
            }
        } else {
            try (ZipFile zip = new ZipFile(zipFile, password.toCharArray())) {
                zip.extractFile(fileToExtract, destinationDirectory);
            }
        }
    }

    private final BiFunction<ZipParameters, String, char[]> passwordProcessor = (zipParameters, password) -> {
        if (Objects.isNull(password)) {
            zipParameters.setEncryptFiles(false);
            return null;
        } else {
            zipParameters.setEncryptFiles(true);
            return password.toCharArray();
        }
    };
}
