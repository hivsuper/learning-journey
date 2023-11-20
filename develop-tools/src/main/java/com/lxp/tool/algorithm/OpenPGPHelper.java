package com.lxp.tool.algorithm;

import org.bouncycastle.bcpg.ArmoredInputStream;
import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.bcpg.BCPGOutputStream;
import org.bouncycastle.bcpg.HashAlgorithmTags;
import org.bouncycastle.bcpg.SymmetricKeyAlgorithmTags;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPCompressedData;
import org.bouncycastle.openpgp.PGPEncryptedData;
import org.bouncycastle.openpgp.PGPEncryptedDataGenerator;
import org.bouncycastle.openpgp.PGPEncryptedDataList;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPKeyPair;
import org.bouncycastle.openpgp.PGPLiteralData;
import org.bouncycastle.openpgp.PGPObjectFactory;
import org.bouncycastle.openpgp.PGPOnePassSignature;
import org.bouncycastle.openpgp.PGPOnePassSignatureList;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyEncryptedData;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPPublicKeyRingCollection;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.openpgp.PGPSecretKeyRingCollection;
import org.bouncycastle.openpgp.PGPSignature;
import org.bouncycastle.openpgp.PGPSignatureGenerator;
import org.bouncycastle.openpgp.PGPSignatureList;
import org.bouncycastle.openpgp.PGPUtil;
import org.bouncycastle.openpgp.jcajce.JcaPGPObjectFactory;
import org.bouncycastle.openpgp.operator.jcajce.JcaKeyFingerprintCalculator;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPContentSignerBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPContentVerifierBuilderProvider;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPDigestCalculatorProviderBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPKeyPair;
import org.bouncycastle.openpgp.operator.jcajce.JcePBESecretKeyDecryptorBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePBESecretKeyEncryptorBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePGPDataEncryptorBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePublicKeyDataDecryptorFactoryBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePublicKeyKeyEncryptionMethodGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.SignatureException;
import java.util.Base64;
import java.util.Date;
import java.util.Iterator;
import java.util.Objects;

public class OpenPGPHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenPGPHelper.class);
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
        KeyPair keyPair = generateKeyPair();
        Date date = new Date();
//        PGPKeyPair pgpEncryptionKeyPair = new JcaPGPKeyPair(PGPPublicKey.RSA_ENCRYPT, keyPair, date);
        PGPKeyPair pgpEncryptionKeyPair = new JcaPGPKeyPair(PGPPublicKey.RSA_GENERAL, keyPair, date);
//        PGPKeyPair pgpSignKeyPair = new JcaPGPKeyPair(PGPPublicKey.RSA_SIGN, keyPair, date);
        PGPKeyPair pgpSignKeyPair = new JcaPGPKeyPair(PGPPublicKey.RSA_GENERAL, keyPair, date);

        PGPSecretKey secretKey = new PGPSecretKey(
                PGPSignature.DEFAULT_CERTIFICATION,
                pgpEncryptionKeyPair,
                identity,
                new JcaPGPDigestCalculatorProviderBuilder().build().get(HashAlgorithmTags.SHA1),
                null,
                null,
                new JcaPGPContentSignerBuilder(pgpSignKeyPair.getPublicKey().getAlgorithm(), HashAlgorithmTags.SHA1),
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
            return readPublicKey(publicKeyRingCollection);
        }
    }

    public static PGPPublicKey readPublicKey(PGPPublicKeyRingCollection publicKeyRingCollection) {
        // we just loop through the collection till we find a key suitable for encryption, in the real
        // world you would probably want to be a bit smarter about this.
        Iterator<PGPPublicKeyRing> iterator = publicKeyRingCollection.getKeyRings();
        while (iterator.hasNext()) {
            PGPPublicKeyRing publicKeyRing = iterator.next();
            Iterator<PGPPublicKey> keyIterator = publicKeyRing.getPublicKeys();
            while (keyIterator.hasNext()) {
                PGPPublicKey key = keyIterator.next();
                if (key.isEncryptionKey()) {
                    return key;
                }
            }
        }
        throw new IllegalArgumentException("Fail to find encryption key");
    }

    public static PGPSecretKey readSecretKey(String secretKeyFile) throws IOException, PGPException {
        try (InputStream in = new ArmoredInputStream(new FileInputStream(secretKeyFile))) {
            PGPSecretKeyRingCollection secretKeyRingCollection = new PGPSecretKeyRingCollection(in, new JcaKeyFingerprintCalculator());
            return readSecretKey(secretKeyRingCollection);
        }
    }

    public static PGPSecretKey readSecretKey(PGPSecretKeyRingCollection secretKeyRingCollection) {
        // we just loop through the collection till we find a key suitable for encryption, in the real
        // world you would probably want to be a bit smarter about this.
        Iterator<PGPSecretKeyRing> iterator = secretKeyRingCollection.getKeyRings();
        while (iterator.hasNext()) {
            PGPSecretKeyRing pgpSecretKeyRing = iterator.next();
            Iterator<PGPSecretKey> keyIterator = pgpSecretKeyRing.getSecretKeys();
            while (keyIterator.hasNext()) {
                PGPSecretKey key = keyIterator.next();
                if (key.isSigningKey()) {
                    return key;
                }
            }
        }
        throw new IllegalArgumentException("Fail to find signature key");
    }

    public static PGPPrivateKey readPrivateKey(String secretKeyFile, long keyId, String password) throws IOException, PGPException {
        try (InputStream in = new ArmoredInputStream(new FileInputStream(secretKeyFile))) {
            PGPSecretKeyRingCollection secretKeyRingCollection = new PGPSecretKeyRingCollection(in, new JcaKeyFingerprintCalculator());
            return readPrivateKey(secretKeyRingCollection, keyId, password.toCharArray());
        }
    }

    public static PGPPrivateKey readPrivateKey(PGPSecretKeyRingCollection secretKeyRingCollection, long keyId, char[] password) throws PGPException {
        PGPSecretKey pgpSecretKey = secretKeyRingCollection.getSecretKey(keyId);
        if (Objects.isNull(pgpSecretKey)) {
            return null;
        }
        return pgpSecretKey.extractPrivateKey(new JcePBESecretKeyDecryptorBuilder().setProvider(BOUNCY_CASTLE_PROVIDER).build(password));
    }

    public static String encryptAndSign(String message, PGPPublicKey publicKey, PGPSecretKeyRingCollection secretKeyRingCollection, String password) throws IOException, PGPException {
        return Base64.getEncoder().encodeToString(encryptAndSign(message, publicKey, secretKeyRingCollection, password.toCharArray()));
    }

    private static byte[] encryptAndSign(String message, PGPPublicKey publicKey, PGPSecretKeyRingCollection secretKeyRingCollection, char[] password) throws IOException, PGPException {
        PGPSecretKey secretKey = readSecretKey(secretKeyRingCollection);
        PGPPrivateKey privateKey = readPrivateKey(secretKeyRingCollection, publicKey.getKeyID(), password);

        ByteArrayOutputStream encryptedData = new ByteArrayOutputStream();
        OutputStream encryptedOut = new ArmoredOutputStream(encryptedData);
        PGPEncryptedDataGenerator encryptedDataGenerator = new PGPEncryptedDataGenerator(
                new JcePGPDataEncryptorBuilder(PGPEncryptedData.CAST5).setWithIntegrityPacket(true).setSecureRandom(new SecureRandom()).setProvider(BOUNCY_CASTLE_PROVIDER)
        );
        encryptedDataGenerator.addMethod(new JcePublicKeyKeyEncryptionMethodGenerator(publicKey).setProvider(BOUNCY_CASTLE_PROVIDER));
        OutputStream encryptedStream = encryptedDataGenerator.open(encryptedOut, new byte[4096]);

        // Write the message to the encrypted stream
        byte[] requestBodyBytes = message.getBytes(StandardCharsets.UTF_8);
        encryptedStream.write(requestBodyBytes);
        encryptedStream.close();

        // Sign the encrypted message using your private key
        ByteArrayOutputStream signedData = new ByteArrayOutputStream();
        OutputStream signedOut = new ArmoredOutputStream(signedData);
        PGPSignatureGenerator signatureGenerator = new PGPSignatureGenerator(
                new JcaPGPContentSignerBuilder(secretKey.getPublicKey().getAlgorithm(), PGPUtil.SHA256).setProvider(BOUNCY_CASTLE_PROVIDER)
        );
        signatureGenerator.init(PGPSignature.BINARY_DOCUMENT, privateKey);
        signatureGenerator.generateOnePassVersion(false).encode(signedOut);
        BCPGOutputStream bcpgOutputStream = new BCPGOutputStream(signedOut);
        bcpgOutputStream.write(requestBodyBytes);
        signatureGenerator.generate().encode(bcpgOutputStream);
        signedOut.close();
        return signedData.toByteArray();
    }

    public static String decryptAndVerify(String message, PGPSecretKeyRingCollection secretKeyRingCollection, String password) throws IOException, PGPException {
//        PGPSecretKey secretKey = readSecretKey(secretKeyRingCollection);
//        PGPPrivateKey privateKey = readPrivateKey(secretKeyRingCollection, publicKey.getKeyID(), password);
        System.out.println(new String(Base64.getDecoder().decode(message.getBytes(StandardCharsets.UTF_8))));

        // Decrypt the response body using your private key and passphrase
        JcaPGPObjectFactory pgpF = new JcaPGPObjectFactory(PGPUtil.getDecoderStream(new ByteArrayInputStream(Base64.getDecoder().decode(message.getBytes(StandardCharsets.UTF_8)))));
        PGPEncryptedDataList enc;
        Object object = pgpF.nextObject();
        if (object instanceof PGPEncryptedDataList) {
            enc = (PGPEncryptedDataList) object;
        } else {
            enc = (PGPEncryptedDataList) pgpF.nextObject();
        }
        PGPOnePassSignatureList onePassSignatureList = (PGPOnePassSignatureList) pgpF.nextObject();
        PGPOnePassSignature onePassSignature = onePassSignatureList.get(0);

        Iterator<?> iterator = enc.getEncryptedDataObjects();
        PGPPrivateKey privateKey = null;
        PGPPublicKeyEncryptedData pbe = null;
        while (privateKey == null && iterator.hasNext()) {
            pbe = (PGPPublicKeyEncryptedData) iterator.next();
            privateKey = readPrivateKey(secretKeyRingCollection, pbe.getKeyID(), password.toCharArray());
        }
        if(Objects.isNull(privateKey)) {
            throw new IllegalArgumentException("fail to find secret key");
        }
        InputStream clear = pbe.getDataStream(new JcePublicKeyDataDecryptorFactoryBuilder().setProvider(BOUNCY_CASTLE_PROVIDER).build(privateKey));
        JcaPGPObjectFactory plainFact = new JcaPGPObjectFactory(clear);
        object = plainFact.nextObject();
        if(object instanceof PGPCompressedData compressedData) {
            InputStream compressedInputStream = new BufferedInputStream(compressedData.getDataStream());
            PGPObjectFactory pgpObjectFactory = new PGPObjectFactory(compressedInputStream, new JcaKeyFingerprintCalculator());
            object = pgpObjectFactory.nextObject();
        }
        if(object instanceof PGPLiteralData literalData) {
            InputStream literalDataInputStream = literalData.getInputStream();
            ByteArrayOutputStream decryptedData = new ByteArrayOutputStream();
            int ch;
            while ((ch = literalDataInputStream.read()) >= 0) {
                onePassSignature.update((byte) ch);
                decryptedData.write(ch);
            }
            byte[] decryptedResponseBody = decryptedData.toByteArray();
            literalDataInputStream.close();
            decryptedData.close();
            return new String(decryptedResponseBody);
        }
//
//        PGPOnePassSignatureList onePassSignatureList = (PGPOnePassSignatureList) plainFact.nextObject();
//        PGPOnePassSignature onePassSignature = onePassSignatureList.get(0);
//        PGPLiteralData literalData = (PGPLiteralData) pgpF.nextObject();
//        InputStream literalDataInputStream = literalData.getInputStream();
//        ByteArrayOutputStream decryptedData = new ByteArrayOutputStream();
//        int ch;
//        while ((ch = literalDataInputStream.read()) >= 0) {
//            onePassSignature.update((byte) ch);
//            decryptedData.write(ch);
//        }
//        byte[] decryptedResponseBody = decryptedData.toByteArray();
//        literalDataInputStream.close();
//        decryptedData.close();
//
//        // Verify the signature of the decrypted response body using the sender's public key
//        PGPPublicKey senderPublicKey = publicKeyRingCollection.getPublicKey(onePassSignature.getKeyID());
//        onePassSignature.init(new JcaPGPContentVerifierBuilderProvider().setProvider("BC"), senderPublicKey);
//        onePassSignature.update(decryptedResponseBody);
//        PGPSignatureList signatureList = (PGPSignatureList) pgpF.nextObject();
//        if (onePassSignature.verify(signatureList.get(0))) {
//            LOGGER.error("Signature verified.");
//        } else {
//            throw new SignatureException("Signature verification failed.");
//        }
//        return new String(decryptedResponseBody);
        return null;
    }
}
