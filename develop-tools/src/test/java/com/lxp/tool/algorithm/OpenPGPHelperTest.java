package com.lxp.tool.algorithm;

import org.bouncycastle.bcpg.ArmoredInputStream;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyRingCollection;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSecretKeyRingCollection;
import org.bouncycastle.openpgp.operator.jcajce.JcaKeyFingerprintCalculator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
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
        final String identify = "Test User";
        final String password = "passphrase";
        OpenPGPHelper.generatePublicKeyAndPrivateKey(identify, password.toCharArray(), publicKeyFile, privateKeyFile);
        Assert.assertNotNull(OpenPGPHelper.readPublicKey(publicKeyFile));
        PGPSecretKey secretKey = OpenPGPHelper.readSecretKey(privateKeyFile);
        Assert.assertNotNull(secretKey);
        Assert.assertNotNull(OpenPGPHelper.readPrivateKey(privateKeyFile, secretKey.getKeyID(), password));
        OpenPGPHelper.readSecretKey(privateKeyFile).getUserIDs().forEachRemaining(userId -> Assert.assertEquals(identify, userId));
    }

    @Test
    public void testEncryptAndSign() throws Exception {
        final String identify = "Test User";
        final String password = "passphrase";
        OpenPGPHelper.generatePublicKeyAndPrivateKey(identify, password.toCharArray(), publicKeyFile, privateKeyFile);
        PGPPublicKey pgpPublicKey = OpenPGPHelper.readPublicKey(publicKeyFile);
        try (InputStream privateKeyIn = new ArmoredInputStream(new FileInputStream(privateKeyFile));
             InputStream publicKeyIn = new ArmoredInputStream(new FileInputStream(publicKeyFile))) {
            PGPSecretKeyRingCollection secretKeyRingCollection = new PGPSecretKeyRingCollection(privateKeyIn, new JcaKeyFingerprintCalculator());
            PGPPublicKeyRingCollection publicKeyRingCollection = new PGPPublicKeyRingCollection(publicKeyIn, new JcaKeyFingerprintCalculator());

            String encrypted = OpenPGPHelper.encryptAndSign("test", pgpPublicKey, secretKeyRingCollection, password);
            System.out.println(encrypted);
            Assert.assertNotNull(encrypted);

            PGPPrivateKey pgpPrivateKey = OpenPGPHelper.readPrivateKey(secretKeyRingCollection, pgpPublicKey.getKeyID(), password.toCharArray());
            String rtn = OpenPGPHelper.decryptAndVerify(encrypted, secretKeyRingCollection, password);
            System.out.println(rtn);
            Assert.assertNotNull(rtn);
        }
    }
}