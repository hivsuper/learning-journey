package com.lxp.tool.algorithm;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * http://www.cnblogs.com/pengxl/p/3967040.html
 */
public class DESHelper {
    private static final String DES = "DES";
    private static final String DES_CBC_PKCS5_PADDING = "DES/CBC/PKCS5Padding";
    private static final String UTF_8 = "UTF-8";

    public static String encrypt(String message, String key) throws Exception {
        Cipher cipher = Cipher.getInstance(DES_CBC_PKCS5_PADDING);
        IvParameterSpec iv = new IvParameterSpec(key.getBytes(UTF_8));
        cipher.init(Cipher.ENCRYPT_MODE, generateSecretKey(key), iv);
        return Base64.getEncoder().encodeToString(cipher.doFinal(message.getBytes(UTF_8)));
    }

    public static String decrypt(String message, String key) throws Exception {
        byte[] bytes = Base64.getDecoder().decode(message);
        Cipher cipher = Cipher.getInstance(DES_CBC_PKCS5_PADDING);
        IvParameterSpec iv = new IvParameterSpec(key.getBytes(UTF_8));
        cipher.init(Cipher.DECRYPT_MODE, generateSecretKey(key), iv);
        byte[] retByte = cipher.doFinal(bytes);
        return new String(retByte);
    }

    private static SecretKey generateSecretKey(String key) throws Exception {
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes(UTF_8));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        return keyFactory.generateSecret(desKeySpec);
    }
}