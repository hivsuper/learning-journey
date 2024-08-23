package com.lxp.tool.pdf;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MergePdfHelperTest {
    private final String packageName = "com/lxp/tool/pdf/";
    private final String destinationFile = "result.pdf";
    private String absolutePath;

    @BeforeEach
    public void setUp() {
        URL url = MergePdfHelperTest.class.getClassLoader().getResource(packageName);
        assertNotNull(url);
        File file = new File(url.getFile());
        absolutePath = file.getAbsolutePath() + File.separator;
    }

    @AfterEach
    public void tearDown() {
        File file = new File(absolutePath + destinationFile);
        System.out.println(file.getAbsolutePath());
        if (file.exists()) {
            assertTrue(file.delete());
        }
    }

    @Test
    public void testMergePdfViaIText() {
        String[] array = new String[]{"pdf1.pdf", "pdf2.pdf"};
        List<InputStream> inputStreams = Arrays.stream(array).map(
                        fileName -> MergePdfHelper.class.getClassLoader().getResourceAsStream(packageName.concat(fileName)))
                .filter(Objects::nonNull).collect(Collectors.toList());
        MergePdfHelper.mergePdfViaIText(inputStreams, absolutePath + destinationFile);
    }
}
