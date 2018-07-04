package com.lxp.tool.pdf;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.junit.Test;

public class MergePdfHelperTest {
    @Test
    public void testMergePdfViaIText() throws Exception {
        String packageName = "com/lxp/tool/pdf/";
        String[] array = new String[] { "pdf1.pdf", "pdf2.pdf" };
        List<InputStream> inputStreams = Arrays.stream(array).map(
                fileName -> MergePdfHelper.class.getClassLoader().getResourceAsStream(packageName.concat(fileName)))
                .filter(Objects::nonNull).collect(Collectors.toList());
        MergePdfHelper.mergePdfViaIText(inputStreams);
    }
}
