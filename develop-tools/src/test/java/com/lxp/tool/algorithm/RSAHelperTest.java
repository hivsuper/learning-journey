package com.lxp.tool.algorithm;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.security.KeyPair;

@RunWith(MockitoJUnitRunner.class)
public class RSAHelperTest {

    @Test
    public void testGetKeyPair() throws Exception {
        KeyPair keyPair = RSAHelper.getKeyPair();
        Assert.assertNotNull(keyPair.getPrivate().getEncoded());
        Assert.assertNotNull(keyPair.getPublic().getEncoded());
    }

    @Test
    public void testDecryptWithPrivateKey() throws Exception {
        Assert.assertEquals("hello world", RSAHelper.decryptWithPrivateKey(
                "KL7xQdFxwtPx/ue6f0i9q4wgWQm1uLOmfehbt8v9ZYUcC2YVz9eEqLEKESAe86tYXbxz7tQXY+HIMDxhlPWrwTgKuzRZ+xCWohR6g9dcDSa7iaKQlYwnO+maW6BMJ3RZQ6JheCVSlwv4hLe3GjdO0ujSlJmqe01IOHSO8Buulnc="));
    }

    @Test
    public void testDecryptWithPublicKey() throws Exception {
        Assert.assertEquals("hello world", RSAHelper.decryptWithPublicKey(
                "Wzrojupw/6cAPOV24mJCbV4e+tRAXWBJOmoI6y7s/fJYZqRdf+Runx1nhx/NI0iCUHahLKlxmwn2d6RV9CFqLcevUPFbvjPU+34sIBFkNTuoVZcstOL9COzUKznxvfGhbyQJ/I7VnI2bCAOn2lsBHmk1qKUgjYqtMT9gWw50xJA="));
    }

    @Test
    public void testEncrypt() throws Exception {
        Assert.assertEquals("hello world",
                RSAHelper.decryptWithPrivateKey(RSAHelper.encryptWithPublicKey("hello world")));
        Assert.assertEquals("hello world",
                RSAHelper.decryptWithPublicKey(RSAHelper.encryptWithPrivateKey("hello world")));
    }

}
