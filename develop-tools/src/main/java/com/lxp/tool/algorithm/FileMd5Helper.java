package com.lxp.tool.algorithm;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileMd5Helper {
    public static String getMD5(String file) {
        String md5Hex;
        try (InputStream inputStream = new FileInputStream(file)) {
            md5Hex = DigestUtils.md5Hex(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return md5Hex;
    }
}
