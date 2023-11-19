package com.lxp.tool.algorithm;

import org.bouncycastle.bcpg.ArmoredInputStream;
import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.bcpg.HashAlgorithmTags;
import org.bouncycastle.bcpg.SymmetricKeyAlgorithmTags;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPKeyPair;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPPublicKeyRingCollection;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.openpgp.PGPSecretKeyRingCollection;
import org.bouncycastle.openpgp.PGPSignature;
import org.bouncycastle.openpgp.operator.jcajce.JcaKeyFingerprintCalculator;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPContentSignerBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPDigestCalculatorProviderBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPKeyPair;
import org.bouncycastle.openpgp.operator.jcajce.JcePBESecretKeyEncryptorBuilder;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Date;

public class OpenPGPHelper {
    private static final String ALGORITHM_RSA = "RSA";
    private static final BouncyCastleProvider BOUNCY_CASTLE_PROVIDER = new BouncyCastleProvider();

    static {
        Security.addProvider(BOUNCY_CASTLE_PROVIDER);
    }

    private static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        // Generate a key pair for signing
        KeyPairGenerator signingKeyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM_RSA, BOUNCY_CASTLE_PROVIDER);
        signingKeyPairGenerator.initialize(2048);
        return signingKeyPairGenerator.generateKeyPair();
    }

    public static void generatePublicKeyAndPrivateKey(String identity, char[] passphrase, String publicKeyFile, String privateKeyFile) throws Exception {
        PGPKeyPair pgpKeyPair = new JcaPGPKeyPair(PGPPublicKey.RSA_SIGN, generateKeyPair(), new Date());
        PGPSecretKey secretKey = new PGPSecretKey(
                PGPSignature.DEFAULT_CERTIFICATION,
                pgpKeyPair,
                identity,
                new JcaPGPDigestCalculatorProviderBuilder().build().get(HashAlgorithmTags.SHA1),
                null,
                null,
                new JcaPGPContentSignerBuilder(pgpKeyPair.getPublicKey().getAlgorithm(), HashAlgorithmTags.SHA1),
                new JcePBESecretKeyEncryptorBuilder(SymmetricKeyAlgorithmTags.AES_256, new JcaPGPDigestCalculatorProviderBuilder().build().get(HashAlgorithmTags.SHA1))
                        .setProvider(BOUNCY_CASTLE_PROVIDER)
                        .build(passphrase)
        );

        PGPPublicKey publicKey = secretKey.getPublicKey();

        try (FileOutputStream publicKeyOut = new FileOutputStream(publicKeyFile);
             FileOutputStream privateKeyOut = new FileOutputStream(privateKeyFile);
             ArmoredOutputStream armoredPublicKeyOut = new ArmoredOutputStream(publicKeyOut);
             ArmoredOutputStream armoredPrivateKeyOut = new ArmoredOutputStream(privateKeyOut)) {
            publicKey.encode(armoredPublicKeyOut);
            armoredPublicKeyOut.close();

            secretKey.encode(armoredPrivateKeyOut);
            armoredPrivateKeyOut.close();
        }
    }

    public static PGPPublicKey readPublicKey(String publicKeyFile) throws IOException, PGPException {
        try (InputStream in = new ArmoredInputStream(new FileInputStream(publicKeyFile))) {
            PGPPublicKeyRingCollection publicKeyRingCollection = new PGPPublicKeyRingCollection(in, new JcaKeyFingerprintCalculator());
            PGPPublicKeyRing publicKeyRing = publicKeyRingCollection.getKeyRings().next();
            return publicKeyRing.getPublicKey();
        }
    }

    public static PGPSecretKey readSecretKey(String secretKeyFile) throws IOException, PGPException {
        try (InputStream in = new ArmoredInputStream(new FileInputStream(secretKeyFile))) {
            PGPSecretKeyRingCollection secretKeyRingCollection = new PGPSecretKeyRingCollection(in, new JcaKeyFingerprintCalculator());
            PGPSecretKeyRing secretKeyRing = secretKeyRingCollection.getKeyRings().next();
            return secretKeyRing.getSecretKey();
        }
    }
}
