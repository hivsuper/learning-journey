package com.lxp.tool.algorithm;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.Arrays;

public class OpenPGPHelperTest {
    private String publicKeyFile;
    private String privateKeyFile;

    @Before
    public void setUp() {
        final String packageName = "com/lxp/tool/algorithm/";
        URL url = OpenPGPHelperTest.class.getClassLoader().getResource(packageName);
        Assert.assertNotNull(url);
        File file = new File(url.getFile());
        final String absolutePath = file.getAbsolutePath() + File.separator;
        publicKeyFile = absolutePath + "private_key.asc";
        privateKeyFile = absolutePath + "public_key.asc";
    }

    @After
    public void tearDown() {
        Arrays.asList(publicKeyFile, privateKeyFile).forEach(key -> {
            File file = new File(key);
            System.out.println(file.getAbsolutePath());
            if (file.exists()) {
                Assert.assertTrue(file.delete());
            }
        });
    }

    @Test
    public void testPublicKeyAndPrivateKey() throws Exception {
        OpenPGPHelper.generatePublicKeyAndPrivateKey("Test User", "passphrase".toCharArray(), publicKeyFile, privateKeyFile);
        Assert.assertNotNull(OpenPGPHelper.readPublicKey(publicKeyFile));
        Assert.assertNotNull(OpenPGPHelper.readSecretKey(privateKeyFile));
        OpenPGPHelper.readSecretKey(privateKeyFile).getUserIDs().forEachRemaining(userId -> Assert.assertEquals("Test User", userId));
    }
}