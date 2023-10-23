package com.lxp.tool.algorithm;

import org.junit.Assert;
import org.junit.Test;

public class DESHelperTest {
    private static final String secrectKey = "12345678";

    @Test
    public void testEncrypt() throws Exception {
        Assert.assertEquals("gZ7N+ROnYHY=", DESHelper.encrypt("message", secrectKey));
    }

    @Test
    public void testDecrypt() throws Exception {
        Assert.assertEquals("message", DESHelper.decrypt("gZ7N+ROnYHY=", secrectKey));
    }

}
