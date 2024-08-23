package com.lxp.tool.algorithm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.KeyPair;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class RSAHelperTest {

    @Test
    public void testGetKeyPair() throws Exception {
        KeyPair keyPair = RSAHelper.getKeyPair();
        assertNotNull(keyPair.getPrivate().getEncoded());
        assertNotNull(keyPair.getPublic().getEncoded());
    }

    @Test
    public void testDecryptWithPrivateKey() throws Exception {
        assertEquals("hello world", RSAHelper.decryptWithPrivateKey(
                "KL7xQdFxwtPx/ue6f0i9q4wgWQm1uLOmfehbt8v9ZYUcC2YVz9eEqLEKESAe86tYXbxz7tQXY+HIMDxhlPWrwTgKuzRZ+xCWohR6g9dcDSa7iaKQlYwnO+maW6BMJ3RZQ6JheCVSlwv4hLe3GjdO0ujSlJmqe01IOHSO8Buulnc="));
    }

    @Test
    public void testDecryptWithPublicKey() throws Exception {
        assertEquals("hello world", RSAHelper.decryptWithPublicKey(
                "Wzrojupw/6cAPOV24mJCbV4e+tRAXWBJOmoI6y7s/fJYZqRdf+Runx1nhx/NI0iCUHahLKlxmwn2d6RV9CFqLcevUPFbvjPU+34sIBFkNTuoVZcstOL9COzUKznxvfGhbyQJ/I7VnI2bCAOn2lsBHmk1qKUgjYqtMT9gWw50xJA="));
    }

    @Test
    public void testEncrypt() throws Exception {
        assertEquals("hello world",
                RSAHelper.decryptWithPrivateKey(RSAHelper.encryptWithPublicKey("hello world")));
        assertEquals("hello world",
                RSAHelper.decryptWithPublicKey(RSAHelper.encryptWithPrivateKey("hello world")));
    }

}
