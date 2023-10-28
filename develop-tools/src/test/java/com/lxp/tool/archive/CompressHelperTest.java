package com.lxp.tool.archive;

import com.lxp.tool.TestHelper;
import com.lxp.tool.algorithm.FileMd5Helper;
import com.lxp.tool.pdf.MergePdfHelperTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;
import java.util.Vector;

public class CompressHelperTest {
    private final String password = "password";
    private final int splitLength = 1024 * 100; // 100KB
    private final String txt1 = "1.txt";
    private final String txt2 = "2.txt";
    private final String txt3 = "3.txt";
    private final String archive = "archive/";
    private final StackWalker walker = StackWalker.getInstance();
    private final List<String> results = new Vector<>();
    private String absolutePath;
    private CompressHelper compressHelper;

    @Before
    public void setUp() {
        String testData = "com/lxp/tool/" + archive;
        URL url = MergePdfHelperTest.class.getClassLoader().getResource(testData);
        Assert.assertNotNull(url);
        File file = new File(url.getFile());
        absolutePath = file.getAbsolutePath() + File.separator;
        compressHelper = new CompressHelper();
    }

    @After
    public void tearDown() {
        results.forEach(folder -> TestHelper.recursiveDelete(new File(folder)));
    }

    @Test
    public void testCompressSingleFileAndExtractSpecificFile() throws IOException {
        Optional<String> methodName = walker.walk(frames -> frames
                .findFirst()
                .map(StackWalker.StackFrame::getMethodName));
        final String resultFolder = absolutePath + methodName.orElseThrow() + File.separator;
        results.add(resultFolder);
        File file = new File(resultFolder);
        if (file.exists()) {
            TestHelper.recursiveDelete(file);
        }
        Assert.assertTrue(file.mkdir());
        String rtn = resultFolder + "single.zip";

        compressHelper.compressSingleFile(absolutePath + txt1, rtn);
        compressHelper.extractSpecificFile(rtn, txt1, resultFolder);

        Assert.assertEquals(FileMd5Helper.getMD5(absolutePath + txt1),
                FileMd5Helper.getMD5(resultFolder + txt1));
    }

    @Test
    public void testCompressSingleFileAndExtractSpecificFileWithPassword() throws IOException {
        Optional<String> methodName = walker.walk(frames -> frames
                .findFirst()
                .map(StackWalker.StackFrame::getMethodName));
        final String resultFolder = absolutePath + methodName.orElseThrow() + File.separator;
        results.add(resultFolder);
        File file = new File(resultFolder);
        if (file.exists()) {
            TestHelper.recursiveDelete(file);
        }
        Assert.assertTrue(file.mkdir());
        String rtn = resultFolder + "single.zip";

        compressHelper.compressSingleFile(absolutePath + txt1, rtn, password);
        compressHelper.extractSpecificFile(rtn, txt1, resultFolder, password);

        Assert.assertEquals(FileMd5Helper.getMD5(absolutePath + txt1),
                FileMd5Helper.getMD5(resultFolder + txt1));
    }

    @Test
    public void testCompressMultipleFilesAndExtract() throws IOException {
        Optional<String> methodName = walker.walk(frames -> frames
                .findFirst()
                .map(StackWalker.StackFrame::getMethodName));
        final String resultFolder = absolutePath + methodName.orElseThrow() + File.separator;
        results.add(resultFolder);
        File file = new File(resultFolder);
        if (file.exists()) {
            TestHelper.recursiveDelete(file);
        }
        Assert.assertTrue(file.mkdir());
        String rtn = resultFolder + "multiple.zip";

        compressHelper.compress(List.of(absolutePath + txt2, absolutePath + txt3), rtn);
        compressHelper.extract(rtn, resultFolder);

        Assert.assertEquals(FileMd5Helper.getMD5(absolutePath + txt2),
                FileMd5Helper.getMD5(resultFolder + txt2));
        Assert.assertEquals(FileMd5Helper.getMD5(absolutePath + txt3),
                FileMd5Helper.getMD5(resultFolder + txt3));
    }

    @Test
    public void testCompressMultipleFilesAndExtractWithPassword() throws IOException {
        Optional<String> methodName = walker.walk(frames -> frames
                .findFirst()
                .map(StackWalker.StackFrame::getMethodName));
        final String resultFolder = absolutePath + methodName.orElseThrow() + File.separator;
        results.add(resultFolder);
        File file = new File(resultFolder);
        if (file.exists()) {
            TestHelper.recursiveDelete(file);
        }
        Assert.assertTrue(file.mkdir());
        String rtn = resultFolder + "multiple.zip";

        compressHelper.compress(List.of(absolutePath + txt2, absolutePath + txt3), rtn, password);
        compressHelper.extract(rtn, resultFolder, password);

        Assert.assertEquals(FileMd5Helper.getMD5(absolutePath + txt2),
                FileMd5Helper.getMD5(resultFolder + txt2));
        Assert.assertEquals(FileMd5Helper.getMD5(absolutePath + txt3),
                FileMd5Helper.getMD5(resultFolder + txt3));
    }

    @Test
    public void testCompressMultipleFilesWithSplitting() throws IOException {
        Optional<String> methodName = walker.walk(frames -> frames
                .findFirst()
                .map(StackWalker.StackFrame::getMethodName));
        final String resultFolder = absolutePath + methodName.orElseThrow() + File.separator;
        results.add(resultFolder);
        File file = new File(resultFolder);
        if (file.exists()) {
            TestHelper.recursiveDelete(file);
        }
        Assert.assertTrue(file.mkdir());
        copy(resultFolder);
        String rtn = resultFolder + "multiple.zip";

        compressHelper.compress(List.of(resultFolder + txt2, resultFolder + txt3), rtn, password, splitLength);

        Assert.assertEquals(2, Optional.ofNullable(new File(resultFolder)
                .listFiles(pathname -> pathname.getName().contains("multiple"))).orElse(new File[]{}).length);
        compressHelper.extract(rtn, resultFolder.concat("zip/"), password);
        Assert.assertEquals(FileMd5Helper.getMD5(resultFolder + txt2),
                FileMd5Helper.getMD5(resultFolder.concat("zip/") + txt2));
        Assert.assertEquals(FileMd5Helper.getMD5(resultFolder + txt3),
                FileMd5Helper.getMD5(resultFolder.concat("zip/") + txt3));
    }


    @Test
    public void testCompressDirectoryAndExtract() throws IOException {
        Optional<String> methodName = walker.walk(frames -> frames
                .findFirst()
                .map(StackWalker.StackFrame::getMethodName));
        final String resultFolder = absolutePath + methodName.orElseThrow() + File.separator;
        results.add(resultFolder);
        File file = new File(resultFolder);
        if (file.exists()) {
            TestHelper.recursiveDelete(file);
        }
        Assert.assertTrue(file.mkdir());
        String rtn = resultFolder + "directory.zip";
        String directory = resultFolder.concat(archive);
        String unzipDirectory = resultFolder.concat("unzip/");
        Assert.assertTrue(new File(unzipDirectory).mkdir());
        TestHelper.copy(List.of(txt1, txt2, txt3), absolutePath, directory);

        compressHelper.compress(directory, rtn);
        compressHelper.extract(rtn, unzipDirectory);

        Assert.assertEquals(FileMd5Helper.getMD5(directory + txt1),
                FileMd5Helper.getMD5(unzipDirectory + archive + txt1));
        Assert.assertEquals(FileMd5Helper.getMD5(directory + txt2),
                FileMd5Helper.getMD5(unzipDirectory + archive + txt2));
        Assert.assertEquals(FileMd5Helper.getMD5(directory + txt3),
                FileMd5Helper.getMD5(unzipDirectory + archive + txt3));
    }

    @Test
    public void testCompressDirectoryAndExtractExtractWithPassword() throws IOException {
        Optional<String> methodName = walker.walk(frames -> frames
                .findFirst()
                .map(StackWalker.StackFrame::getMethodName));
        final String resultFolder = absolutePath + methodName.orElseThrow() + File.separator;
        results.add(resultFolder);
        File file = new File(resultFolder);
        if (file.exists()) {
            TestHelper.recursiveDelete(file);
        }
        Assert.assertTrue(file.mkdir());
        String rtn = resultFolder + "directory.zip";
        String directory = resultFolder.concat(archive);
        String unzipDirectory = resultFolder.concat("unzip/");
        Assert.assertTrue(new File(unzipDirectory).mkdir());
        TestHelper.copy(List.of(txt1, txt2, txt3), absolutePath, directory);

        compressHelper.compress(directory, rtn, password);
        compressHelper.extract(rtn, unzipDirectory, password);

        Assert.assertEquals(FileMd5Helper.getMD5(directory + txt1),
                FileMd5Helper.getMD5(unzipDirectory +archive + txt1));
        Assert.assertEquals(FileMd5Helper.getMD5(directory + txt2),
                FileMd5Helper.getMD5(unzipDirectory +archive + txt2));
        Assert.assertEquals(FileMd5Helper.getMD5(directory + txt3),
                FileMd5Helper.getMD5(unzipDirectory +archive + txt3));
    }

    @Test
    public void testCompressDirectoryAndExtractWithSplitting() throws IOException {
        Optional<String> methodName = walker.walk(frames -> frames
                .findFirst()
                .map(StackWalker.StackFrame::getMethodName));
        final String resultFolder = absolutePath + methodName.orElseThrow() + File.separator;
        results.add(resultFolder);
        File file = new File(resultFolder);
        if (file.exists()) {
            TestHelper.recursiveDelete(file);
        }
        Assert.assertTrue(file.mkdir());
        copy(resultFolder);
        String rtn = resultFolder + "directory.zip";

        compressHelper.compress(resultFolder, rtn, password, splitLength);

        Assert.assertEquals(3, Optional.ofNullable(new File(resultFolder)
                .listFiles(pathname -> pathname.getName().contains("directory"))).orElse(new File[]{}).length);
        compressHelper.extract(rtn, resultFolder.concat("zip/"), password);
        Assert.assertEquals(FileMd5Helper.getMD5(resultFolder + txt1),
                FileMd5Helper.getMD5(resultFolder.concat("zip/") + file.getName() + File.separator + txt1));
        Assert.assertEquals(FileMd5Helper.getMD5(resultFolder + txt2),
                FileMd5Helper.getMD5(resultFolder.concat("zip/") + file.getName() + File.separator + txt2));
        Assert.assertEquals(FileMd5Helper.getMD5(resultFolder + txt3),
                FileMd5Helper.getMD5(resultFolder.concat("zip/") + file.getName() + File.separator + txt3));
    }

    @Test
    public void testCompressAppendDirectory() throws IOException {
        Optional<String> methodName = walker.walk(frames -> frames
                .findFirst()
                .map(StackWalker.StackFrame::getMethodName));
        final String resultFolder = absolutePath + methodName.orElseThrow() + File.separator;
        results.add(resultFolder);
        File file = new File(resultFolder);
        if (file.exists()) {
            TestHelper.recursiveDelete(file);
        }
        Assert.assertTrue(file.mkdir());
        String rtn = resultFolder + "append.zip";
        compressHelper.compress(absolutePath + txt1, rtn);
        String directory = resultFolder.concat(archive);
        String unzipDirectory = resultFolder.concat("unzip/");
        TestHelper.copy(List.of(txt1, txt2, txt3), absolutePath, directory);

        compressHelper.compressAppend(directory, rtn);

        compressHelper.extract(rtn, unzipDirectory);
        Assert.assertEquals(FileMd5Helper.getMD5(absolutePath + txt1),
                FileMd5Helper.getMD5(unzipDirectory + txt1));
        Assert.assertEquals(FileMd5Helper.getMD5(directory + txt1),
                FileMd5Helper.getMD5(unzipDirectory +archive + txt1));
        Assert.assertEquals(FileMd5Helper.getMD5(directory + txt2),
                FileMd5Helper.getMD5(unzipDirectory +archive + txt2));
        Assert.assertEquals(FileMd5Helper.getMD5(directory + txt3),
                FileMd5Helper.getMD5(unzipDirectory +archive + txt3));
    }

    @Test
    public void testCompressAppendDirectoryWithPassword() throws IOException {
        Optional<String> methodName = walker.walk(frames -> frames
                .findFirst()
                .map(StackWalker.StackFrame::getMethodName));
        final String resultFolder = absolutePath + methodName.orElseThrow() + File.separator;
        results.add(resultFolder);
        File file = new File(resultFolder);
        if (file.exists()) {
            TestHelper.recursiveDelete(file);
        }
        Assert.assertTrue(file.mkdir());
        String rtn = resultFolder + "append.zip";
        compressHelper.compress(absolutePath + txt1, rtn, password);
        String directory = resultFolder.concat(archive);
        String unzipDirectory = resultFolder.concat("unzip/");
        TestHelper.copy(List.of(txt1, txt2, txt3), absolutePath, directory);

        compressHelper.compressAppend(absolutePath, rtn, password);

        compressHelper.extract(rtn, unzipDirectory, password);
        Assert.assertEquals(FileMd5Helper.getMD5(absolutePath + txt1),
                FileMd5Helper.getMD5(unzipDirectory + txt1));
        Assert.assertEquals(FileMd5Helper.getMD5(directory + txt1),
                FileMd5Helper.getMD5(unzipDirectory + archive + txt1));
        Assert.assertEquals(FileMd5Helper.getMD5(directory + txt2),
                FileMd5Helper.getMD5(unzipDirectory + archive + txt2));
        Assert.assertEquals(FileMd5Helper.getMD5(directory + txt3),
                FileMd5Helper.getMD5(unzipDirectory + archive + txt3));
    }

    @Test
    public void testCompressAppendSingleFile() throws IOException {
        Optional<String> methodName = walker.walk(frames -> frames
                .findFirst()
                .map(StackWalker.StackFrame::getMethodName));
        final String resultFolder = absolutePath + methodName.orElseThrow() + File.separator;
        results.add(resultFolder);
        File file = new File(resultFolder);
        if (file.exists()) {
            TestHelper.recursiveDelete(file);
        }
        Assert.assertTrue(file.mkdir());
        String rtn = resultFolder + "append.zip";
        compressHelper.compress(absolutePath + txt1, rtn);

        compressHelper.compressAppend(absolutePath + txt2, rtn);

        compressHelper.extract(rtn, resultFolder);

        Assert.assertEquals(FileMd5Helper.getMD5(absolutePath + txt1),
                FileMd5Helper.getMD5(resultFolder + txt1));
        Assert.assertEquals(FileMd5Helper.getMD5(absolutePath + txt2),
                FileMd5Helper.getMD5(resultFolder + txt2));
    }

    @Test
    public void testCompressAppendSingleFileWithPassword() throws IOException {
        Optional<String> methodName = walker.walk(frames -> frames
                .findFirst()
                .map(StackWalker.StackFrame::getMethodName));
        final String resultFolder = absolutePath + methodName.orElseThrow() + File.separator;
        results.add(resultFolder);
        File file = new File(resultFolder);
        if (file.exists()) {
            TestHelper.recursiveDelete(file);
        }
        Assert.assertTrue(file.mkdir());
        String rtn = resultFolder + "append.zip";
        compressHelper.compress(absolutePath + txt1, rtn, password);

        compressHelper.compressAppend(absolutePath + txt2, rtn, password);

        compressHelper.extract(rtn, resultFolder, password);
        Assert.assertEquals(FileMd5Helper.getMD5(absolutePath + txt1),
                FileMd5Helper.getMD5(resultFolder + txt1));
        Assert.assertEquals(FileMd5Helper.getMD5(absolutePath + txt2),
                FileMd5Helper.getMD5(resultFolder + txt2));
    }

    @Test
    public void testCompressAppendMultipleFiles() throws IOException {
        Optional<String> methodName = walker.walk(frames -> frames
                .findFirst()
                .map(StackWalker.StackFrame::getMethodName));
        final String resultFolder = absolutePath + methodName.orElseThrow() + File.separator;
        results.add(resultFolder);
        File file = new File(resultFolder);
        if (file.exists()) {
            TestHelper.recursiveDelete(file);
        }
        Assert.assertTrue(file.mkdir());
        String rtn = resultFolder + "append.zip";
        compressHelper.compress(absolutePath + txt1, rtn);

        compressHelper.compressAppend(List.of(absolutePath + txt2, absolutePath + txt3), rtn);

        compressHelper.extract(rtn, resultFolder);

        Assert.assertEquals(FileMd5Helper.getMD5(absolutePath + txt1),
                FileMd5Helper.getMD5(resultFolder + txt1));
        Assert.assertEquals(FileMd5Helper.getMD5(absolutePath + txt2),
                FileMd5Helper.getMD5(resultFolder + txt2));
        Assert.assertEquals(FileMd5Helper.getMD5(absolutePath + txt3),
                FileMd5Helper.getMD5(resultFolder + txt3));
    }

    @Test
    public void testCompressAppendMultipleFilesWithPassword() throws IOException {
        Optional<String> methodName = walker.walk(frames -> frames
                .findFirst()
                .map(StackWalker.StackFrame::getMethodName));
        final String resultFolder = absolutePath + methodName.orElseThrow() + File.separator;
        results.add(resultFolder);
        File file = new File(resultFolder);
        if (file.exists()) {
            TestHelper.recursiveDelete(file);
        }
        Assert.assertTrue(file.mkdir());
        String rtn = resultFolder + "append.zip";
        compressHelper.compress(absolutePath + txt1, rtn, password);

        compressHelper.compressAppend(List.of(absolutePath + txt2, absolutePath + txt3), rtn, password);

        compressHelper.extract(rtn, resultFolder, password);
        Assert.assertEquals(FileMd5Helper.getMD5(absolutePath + txt1),
                FileMd5Helper.getMD5(resultFolder + txt1));
        Assert.assertEquals(FileMd5Helper.getMD5(absolutePath + txt2),
                FileMd5Helper.getMD5(resultFolder + txt2));
        Assert.assertEquals(FileMd5Helper.getMD5(absolutePath + txt3),
                FileMd5Helper.getMD5(resultFolder + txt3));
    }

    private void copy(String destinationPath) {
        // minimum allowed split length of 65536 Bytes
        List.of(txt1, txt2, txt3).forEach(f -> {
            try {
                String content = String.join(System.lineSeparator(), Files.readAllLines(Paths.get(absolutePath + f))) + System.lineSeparator();
                Files.writeString(Paths.get(destinationPath + f), content.repeat(5000000), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}