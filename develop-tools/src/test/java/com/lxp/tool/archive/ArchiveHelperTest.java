package com.lxp.tool.archive;

import com.lxp.tool.TestHelper;
import com.lxp.tool.algorithm.FileMd5Helper;
import com.lxp.tool.pdf.MergePdfHelperTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ArchiveHelperTest {
    private final String txt1 = "1.txt";
    private final String txt2 = "2.txt";
    private final String txt3 = "3.txt";
    private final String archive = "archive/";
    private final StackWalker walker = StackWalker.getInstance();
    private final List<String> results = new Vector<>();
    private String absolutePath;
    private ArchiveHelper archiveHelper;

    @BeforeEach
    public void setUp() {
        String testData = "com/lxp/tool/" + archive;
        URL url = MergePdfHelperTest.class.getClassLoader().getResource(testData);
        assertNotNull(url);
        File file = new File(url.getFile());
        absolutePath = file.getAbsolutePath() + File.separator;
        archiveHelper = new ArchiveHelper();
    }

    @AfterEach
    public void tearDown() {
        results.forEach(folder -> TestHelper.recursiveDelete(new File(folder)));
    }

    @Test
    public void zipSingleFile() throws IOException {
        Optional<String> methodName = walker.walk(frames -> frames
                .findFirst()
                .map(StackWalker.StackFrame::getMethodName));
        final String resultFolder = absolutePath + methodName.orElseThrow() + File.separator;
        results.add(resultFolder);
        File file = new File(resultFolder);
        if (file.exists()) {
            TestHelper.recursiveDelete(file);
        }
        assertTrue(file.mkdir());
        String rtn = resultFolder + "single.zip";

        archiveHelper.zip(absolutePath + txt1, rtn);

        archiveHelper.unzip(rtn, resultFolder);
        assertEquals(FileMd5Helper.getMD5(absolutePath + txt1),
                FileMd5Helper.getMD5(resultFolder + txt1));
    }

    @Test
    public void zipMultipleFiles() throws IOException {
        Optional<String> methodName = walker.walk(frames -> frames
                .findFirst()
                .map(StackWalker.StackFrame::getMethodName));
        final String resultFolder = absolutePath + methodName.orElseThrow() + File.separator;
        results.add(resultFolder);
        File file = new File(resultFolder);
        if (file.exists()) {
            TestHelper.recursiveDelete(file);
        }
        assertTrue(file.mkdir());
        String rtn = resultFolder + "multiple.zip";

        archiveHelper.zip(List.of(absolutePath + txt2, absolutePath + txt3), rtn);

        archiveHelper.unzip(rtn, resultFolder);
        assertEquals(FileMd5Helper.getMD5(absolutePath + txt2),
                FileMd5Helper.getMD5(resultFolder + txt2));
        assertEquals(FileMd5Helper.getMD5(absolutePath + txt3),
                FileMd5Helper.getMD5(resultFolder + txt3));
    }

    @Test
    public void appFileToZippedFile() throws IOException {
        Optional<String> methodName = walker.walk(frames -> frames
                .findFirst()
                .map(StackWalker.StackFrame::getMethodName));
        final String resultFolder = absolutePath + methodName.orElseThrow() + File.separator;
        results.add(resultFolder);
        File file = new File(resultFolder);
        if (file.exists()) {
            TestHelper.recursiveDelete(file);
        }
        assertTrue(file.mkdir());
        String rtn1 = resultFolder + "append1.zip";
        String rtn2 = resultFolder + "append2.zip";
        archiveHelper.zip(absolutePath + txt1, rtn1);
        archiveHelper.zip(List.of(absolutePath + txt1,
                absolutePath + txt2, absolutePath + txt3), rtn2);

        archiveHelper.zipAppend(absolutePath + txt2, rtn1);
        archiveHelper.zipAppend(absolutePath + txt3, rtn1);

        String result1 = resultFolder + "1" + File.separator;
        String result2 = resultFolder + "2" + File.separator;
        archiveHelper.unzip(rtn1, result1);
        archiveHelper.unzip(rtn2, result2);

        assertEquals(FileMd5Helper.getMD5(result1 + txt1),
                FileMd5Helper.getMD5(result1 + txt1));
        assertEquals(FileMd5Helper.getMD5(result1 + txt2),
                FileMd5Helper.getMD5(result1 + txt2));
        assertEquals(FileMd5Helper.getMD5(result1 + txt3),
                FileMd5Helper.getMD5(result1 + txt3));
    }

    @Test
    public void zipDirectory() throws IOException {
        Optional<String> methodName = walker.walk(frames -> frames
                .findFirst()
                .map(StackWalker.StackFrame::getMethodName));
        final String resultFolder = absolutePath + methodName.orElseThrow() + File.separator;
        results.add(resultFolder);
        File file = new File(resultFolder);
        if (file.exists()) {
            TestHelper.recursiveDelete(file);
        }
        assertTrue(file.mkdir());
        String rtn = resultFolder + "directory.zip";
        // Copy test files to a new folder to keep data clean
        String directory = resultFolder.concat(archive);
        String unzipDirectory = resultFolder.concat("unzip/");
        assertTrue(new File(unzipDirectory).mkdir());
        TestHelper.copy(List.of(txt1, txt2, txt3), absolutePath, directory);

        archiveHelper.zip(directory, rtn);

        archiveHelper.unzip(rtn, unzipDirectory);
        assertEquals(FileMd5Helper.getMD5(absolutePath + txt1),
                FileMd5Helper.getMD5(unzipDirectory + archive + txt1));
        assertEquals(FileMd5Helper.getMD5(absolutePath + txt2),
                FileMd5Helper.getMD5(unzipDirectory + archive + txt2));
        assertEquals(FileMd5Helper.getMD5(absolutePath + txt3),
                FileMd5Helper.getMD5(unzipDirectory + archive + txt3));
    }
}