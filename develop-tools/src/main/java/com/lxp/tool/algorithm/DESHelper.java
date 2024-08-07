package com.lxp.tool.algorithm;

import lombok.experimental.UtilityClass;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * <a href="http://www.cnblogs.com/pengxl/p/3967040.html">...</a>
 */
@UtilityClass
public class DESHelper {
    private static final String DES = "DES";
    private static final String DES_CBC_PKCS5_PADDING = "DES/CBC/PKCS5Padding";

    public static String encrypt(String message, String key) throws Exception {
        Cipher cipher = Cipher.getInstance(DES_CBC_PKCS5_PADDING);
        IvParameterSpec iv = new IvParameterSpec(key.getBytes(StandardCharsets.UTF_8));
        cipher.init(Cipher.ENCRYPT_MODE, generateSecretKey(key), iv);
        return Base64.getEncoder().encodeToString(cipher.doFinal(message.getBytes(StandardCharsets.UTF_8)));
    }

    public static String decrypt(String message, String key) throws Exception {
        byte[] bytes = Base64.getDecoder().decode(message);
        Cipher cipher = Cipher.getInstance(DES_CBC_PKCS5_PADDING);
        IvParameterSpec iv = new IvParameterSpec(key.getBytes(StandardCharsets.UTF_8));
        cipher.init(Cipher.DECRYPT_MODE, generateSecretKey(key), iv);
        byte[] retByte = cipher.doFinal(bytes);
        return new String(retByte);
    }

    private static SecretKey generateSecretKey(String key) throws Exception {
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes(StandardCharsets.UTF_8));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        return keyFactory.generateSecret(desKeySpec);
    }
}