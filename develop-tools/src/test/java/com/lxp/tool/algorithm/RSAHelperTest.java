package com.lxp.tool.algorithm;

import java.security.KeyPair;
import java.util.Base64;

import org.junit.Assert;
import org.junit.Test;

public class RSAHelperTest {

    @Test
    public void testGetKeyPair() throws Exception {
        KeyPair keyPair = RSAHelper.getKeyPair();
        Assert.assertTrue(new String(Base64.getEncoder().encode(keyPair.getPrivate().getEncoded())) != null);
        Assert.assertTrue(new String(Base64.getEncoder().encode(keyPair.getPublic().getEncoded())) != null);
    }

    @Test
    public void testDecrypt() throws Exception {
        Assert.assertEquals("hello world", RSAHelper.decrypt(
                "KL7xQdFxwtPx/ue6f0i9q4wgWQm1uLOmfehbt8v9ZYUcC2YVz9eEqLEKESAe86tYXbxz7tQXY+HIMDxhlPWrwTgKuzRZ+xCWohR6g9dcDSa7iaKQlYwnO+maW6BMJ3RZQ6JheCVSlwv4hLe3GjdO0ujSlJmqe01IOHSO8Buulnc="));
    }

    @Test
    public void testEncrypt() throws Exception {
        Assert.assertEquals("hello world", RSAHelper.decrypt(RSAHelper.encrypt("hello world")));
    }

}
