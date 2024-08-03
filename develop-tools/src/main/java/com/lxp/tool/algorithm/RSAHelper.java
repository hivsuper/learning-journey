package com.lxp.tool.algorithm;

import lombok.experimental.UtilityClass;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

/**
 * <a href="https://blog.csdn.net/qq_18870023/article/details/52596808">java加解密之RSA使用</a>
 */
@UtilityClass
public class RSAHelper {
    private static final String ALGORITHM = "RSA";
    private static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCEAM61WD8qUILgHpc0SPQ+qZ4pI99gg/heNNxnThbMDwKIZ9OmOCzUig63JU9SpLtubWs80dWvUfckYtMStSRLApxIq2wF+STcP6JQd86etWEfyKWFOeAg/xfCVlWvEFJGes8m4p1Xz0nilluzRF7Uugh6iT3GnjPkxbYUYG2frwIDAQAB";
    private static final String PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIQAzrVYPypQguAelzRI9D6pnikj32CD+F403GdOFswPAohn06Y4LNSKDrclT1Kku25tazzR1a9R9yRi0xK1JEsCnEirbAX5JNw/olB3zp61YR/IpYU54CD/F8JWVa8QUkZ6zybinVfPSeKWW7NEXtS6CHqJPcaeM+TFthRgbZ+vAgMBAAECgYASaV5rktx0lXg7tGiYpVhT6SqjSFqexql80livnAe3/Jv41KqDs7AlEQCOcQjIWSu/ckmT2Ry58G/NRm90W4mtLGJXZRRPY0gZe85TZ6uqbydjLnFLbdLTlqqhQ7Ho1M0qf/wVa8hG9L7obtMrGNe0+fXvQGHcPYRTMh8O8PwKgQJBAOD6/WnxgfC4jEhZcBxm8vXy27BMkyqRx3z8oYgb2Aeb8UHDuf2mEc+kVak7XXLCe82lS5ZVzTetWrJAjZDOScECQQCWNAzKkx6iv7Eezlh5OqKCzawHay5L9Y9gmuRcNbyFT0N2h13/xptb/Dkc2/KQ+qXHwiw5MoIHmTL+a3FbguVvAkB6bhuZm6NqrAMrHAC2dA+jaBXFSuaLM+mrSo9c0kotWd9gOnLsZYMry/IRtgyUduGId3s6xFZ5Bg1lE1v2gUwBAkBh7qnwYp/06RsoxM02rZ5LvqHNbHqRGBhVmmgOHxM9sAKCg5wsKmgdP4RVZqrvEB2K8vU2jdHt9lsjOxZ73gqNAkEAvVJOjRh+N0L+9gscwIdK++ZYYhOygKoU2OfGF1VHtZzU7zsk03e5s1y4sURF/9aumqZ1HMaOFyoKUs5oPyhsDQ==";

    public static KeyPair getKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
        keyPairGenerator.initialize(1024);
        return keyPairGenerator.generateKeyPair();
    }

    public static String decryptWithPublicKey(String content) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, getPublicKey());
        return new String(cipher.doFinal(Base64.getDecoder().decode(content)));
    }

    public static String decryptWithPrivateKey(String content) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, getPrivateKey());
        return new String(cipher.doFinal(Base64.getDecoder().decode(content)));
    }

    public static String encryptWithPrivateKey(String content) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, getPrivateKey());
        return new String(Base64.getEncoder().encode(cipher.doFinal(content.getBytes())));
    }

    public static String encryptWithPublicKey(String content) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey());
        return new String(Base64.getEncoder().encode(cipher.doFinal(content.getBytes())));
    }

    private static PrivateKey getPrivateKey() throws Exception {
        byte[] privateKeyCode = Base64.getDecoder().decode(PRIVATE_KEY.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyCode);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        return keyFactory.generatePrivate(keySpec);
    }

    private static PublicKey getPublicKey() throws Exception {
        byte[] publicKeyCode = Base64.getDecoder().decode(PUBLIC_KEY.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyCode);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        return keyFactory.generatePublic(keySpec);
    }
}
