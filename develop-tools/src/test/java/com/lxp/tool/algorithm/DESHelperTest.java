package com.lxp.tool.algorithm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DESHelperTest {
    private static final String secrectKey = "12345678";

    @Test
    public void testEncrypt() throws Exception {
        assertEquals("gZ7N+ROnYHY=", DESHelper.encrypt("message", secrectKey));
    }

    @Test
    public void testDecrypt() throws Exception {
        assertEquals("message", DESHelper.decrypt("gZ7N+ROnYHY=", secrectKey));
    }

}
