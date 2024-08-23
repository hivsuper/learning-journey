package com.lxp.tool.algorithm;

import org.bouncycastle.bcpg.ArmoredInputStream;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.util.encoders.Base64;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OpenPGPHelperTest {
    private String publicKeyFile;
    private String privateKeyFile;

    @BeforeEach
    public void setUp() {
        final String packageName = "com/lxp/tool/algorithm/";
        URL url = OpenPGPHelperTest.class.getClassLoader().getResource(packageName);
        assertNotNull(url);
        File file = new File(url.getFile());
        final String absolutePath = file.getAbsolutePath() + File.separator;
        publicKeyFile = absolutePath + "public_key.asc";
        privateKeyFile = absolutePath + "private_key.asc";
    }

    @AfterEach
    public void tearDown() {
        List.of(publicKeyFile, privateKeyFile).forEach(key -> {
            File file = new File(key);
            System.out.println(file.getAbsolutePath());
            if (file.exists()) {
                assertTrue(file.delete());
            }
        });
    }

    @Test
    public void testEncryptAndSign() throws Exception {
        final String identify = "Test User";
        final String password = "passphrase";
        RSAKeyPairGenerator.generate(publicKeyFile, privateKeyFile, identify, password);
        String response;
        String payload = "test";
        try (InputStream inputStream = new ByteArrayInputStream(payload.getBytes(StandardCharsets.UTF_8));
             ByteArrayOutputStream resultOutputStream = new ByteArrayOutputStream();
             InputStream privateKeyIn = new ArmoredInputStream(new FileInputStream(privateKeyFile));
             InputStream publicKeyIn = new ArmoredInputStream(new FileInputStream(publicKeyFile))) {

            PGPPublicKey publicKey = OpenPGPHelper.getInstance().readPublicKey(publicKeyIn);
            PGPPrivateKey privateKey = OpenPGPHelper.getInstance().findSecretKey(OpenPGPHelper.getInstance().readSecretKey(privateKeyIn), password.toCharArray());

            OpenPGPHelper.getInstance().encryptAndSign(resultOutputStream, inputStream, publicKey, privateKey, identify);
            response = Base64.toBase64String(resultOutputStream.toByteArray());
            assertNotNull(response);
        }

        try (InputStream inputStream = new ByteArrayInputStream(Base64.decode(response));
             ByteArrayOutputStream resultOutputStream = new ByteArrayOutputStream();
             InputStream privateKeyIn = new ArmoredInputStream(new FileInputStream(privateKeyFile));
             BufferedInputStream publicKeyIn = new BufferedInputStream(new FileInputStream(publicKeyFile))) {
            OpenPGPHelper.getInstance().decryptAndVerify(inputStream, resultOutputStream, publicKeyIn, privateKeyIn, password);
            assertEquals(payload, resultOutputStream.toString());
        }
    }
}