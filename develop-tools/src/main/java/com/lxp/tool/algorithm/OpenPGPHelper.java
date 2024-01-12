package com.lxp.tool.algorithm;

import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPCompressedData;
import org.bouncycastle.openpgp.PGPCompressedDataGenerator;
import org.bouncycastle.openpgp.PGPEncryptedData;
import org.bouncycastle.openpgp.PGPEncryptedDataGenerator;
import org.bouncycastle.openpgp.PGPEncryptedDataList;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPLiteralData;
import org.bouncycastle.openpgp.PGPLiteralDataGenerator;
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
import org.bouncycastle.openpgp.PGPSignatureSubpacketGenerator;
import org.bouncycastle.openpgp.PGPUtil;
import org.bouncycastle.openpgp.operator.PublicKeyDataDecryptorFactory;
import org.bouncycastle.openpgp.operator.jcajce.JcaKeyFingerprintCalculator;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPContentSignerBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPContentVerifierBuilderProvider;
import org.bouncycastle.openpgp.operator.jcajce.JcePBESecretKeyDecryptorBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePGPDataEncryptorBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePublicKeyDataDecryptorFactoryBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePublicKeyKeyEncryptionMethodGenerator;
import org.bouncycastle.util.io.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.security.Security;
import java.security.SignatureException;
import java.util.Date;
import java.util.Iterator;
import java.util.Objects;

public class OpenPGPHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenPGPHelper.class);
    private static final BouncyCastleProvider BOUNCY_CASTLE_PROVIDER = new BouncyCastleProvider();

    private static OpenPGPHelper instance;

    private OpenPGPHelper() {
    }

    public static OpenPGPHelper getInstance() {

        if (instance == null) {
            instance = new OpenPGPHelper();
        }
        return instance;
    }


    public PGPPublicKey readPublicKey(InputStream in) throws IOException, PGPException {

        PGPPublicKeyRingCollection pgpPub = new PGPPublicKeyRingCollection(PGPUtil.getDecoderStream(in),
                new JcaKeyFingerprintCalculator());

        PGPPublicKey key = null;

        //
        // iterate through the key rings.
        //
        Iterator<PGPPublicKeyRing> rIt = pgpPub.getKeyRings();

        while (key == null && rIt.hasNext()) {
            PGPPublicKeyRing kRing = rIt.next();
            Iterator<PGPPublicKey> kIt = kRing.getPublicKeys();
            while (kIt.hasNext()) {  //(key == null && kIt.hasNext())
                PGPPublicKey k = kIt.next();
                LOGGER.debug("Is Encryption Key =" + k.isEncryptionKey());
                LOGGER.debug("Is Master Key =" + k.isMasterKey());
                LOGGER.debug("Is Revoked Key =" + k.hasRevocation());
                LOGGER.debug(" Key ID =" + k.getKeyID());
                LOGGER.debug(" Key Strength =" + k.getBitStrength());
                if (k.isEncryptionKey()) {
                    key = k;
                }
            }
        }

        if (key == null) {
            throw new IllegalArgumentException("Can't find encryption key in key ring.");
        }

        return key;
    }

    public PGPPrivateKey findSecretKey(PGPSecretKey pgpSecKey, char[] pass)
            throws PGPException {
        return pgpSecKey.extractPrivateKey(new JcePBESecretKeyDecryptorBuilder().setProvider(new BouncyCastleProvider()).build(pass));
    }

    static PGPPrivateKey findSecretKey(PGPSecretKeyRingCollection pgpSec, long keyID, char[] pass)
            throws PGPException {
        PGPSecretKey pgpSecKey = pgpSec.getSecretKey(keyID);

        if (pgpSecKey == null) {
            return null;
        }

        return pgpSecKey.extractPrivateKey(new JcePBESecretKeyDecryptorBuilder().setProvider(new BouncyCastleProvider()).build(pass));
    }

    public PGPSecretKey readSecretKey(InputStream input) throws IOException, PGPException {
        PGPSecretKeyRingCollection pgpSec = new PGPSecretKeyRingCollection(
                PGPUtil.getDecoderStream(input), new JcaKeyFingerprintCalculator());

        //
        // we just loop through the collection till we find a key suitable for encryption, in the real
        // world you would probably want to be a bit smarter about this.
        //

        Iterator<PGPSecretKeyRing> keyRingIter = pgpSec.getKeyRings();
        while (keyRingIter.hasNext()) {
            PGPSecretKeyRing keyRing = keyRingIter.next();

            Iterator<PGPSecretKey> keyIter = keyRing.getSecretKeys();
            while (keyIter.hasNext()) {
                PGPSecretKey key = keyIter.next();

                if (key.isSigningKey()) {
                    return key;
                }
            }
        }

        throw new IllegalArgumentException("Can't find signing key in key ring.");
    }

    public void encryptAndSign(OutputStream out, InputStream inputStream, PGPPublicKey encKey, PGPPrivateKey privateKey, String identify) {
        out = new ArmoredOutputStream(out);
        try {
            PGPEncryptedDataGenerator encGen =
                    new PGPEncryptedDataGenerator(
                            new JcePGPDataEncryptorBuilder(PGPEncryptedData.AES_256).setWithIntegrityPacket(true).setSecureRandom(
                                            new SecureRandom())
                                    .setProvider(new BouncyCastleProvider()));
            encGen.addMethod(new JcePublicKeyKeyEncryptionMethodGenerator(encKey).setProvider(new BouncyCastleProvider()));
            OutputStream encryptedOut = encGen.open(out, new byte[inputStream.available()]);

            PGPCompressedDataGenerator comData = new PGPCompressedDataGenerator(PGPCompressedData.ZIP);
            OutputStream compressedData = comData.open(encryptedOut);

            PGPSignatureGenerator sGen = new PGPSignatureGenerator(new JcaPGPContentSignerBuilder(
                    privateKey.getPublicKeyPacket().getAlgorithm(), PGPUtil.SHA512).setProvider(new BouncyCastleProvider()));
            sGen.init(PGPSignature.BINARY_DOCUMENT, privateKey);
            Iterator<String> it = encKey.getUserIDs();
            if (it.hasNext()) {
                PGPSignatureSubpacketGenerator spGen = new PGPSignatureSubpacketGenerator();
                spGen.addSignerUserID(false, it.next());
                sGen.setHashedSubpackets(spGen.generate());
            }
            //BCPGOutputStream bOut = new BCPGOutputStream(compressedData);
            sGen.generateOnePassVersion(false).encode(compressedData); // bOut

            PGPLiteralDataGenerator lGen = new PGPLiteralDataGenerator();
            OutputStream lOut = lGen.open(compressedData, PGPLiteralData.BINARY, identify, new Date(),
                    new byte[inputStream.available()]); //bOut

            byte[] data = inputStream.readAllBytes();
            lOut.write(data);
            sGen.update(data);

            lOut.close();
            lGen.close();

            sGen.generate().encode(compressedData);

            //bOut.close();

            comData.close();
            compressedData.close();

            encryptedOut.close();
            encGen.close();

            out.close();
        } catch (PGPException e) {
            if (e.getUnderlyingException() != null) {
                LOGGER.error(e.getUnderlyingException().getMessage(), e.getUnderlyingException());
            } else {
                LOGGER.error(e.getMessage(), e);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void decryptAndVerify(InputStream in, OutputStream out, BufferedInputStream publicKeyStream, InputStream privateKeyStream, String password)
            throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        PGPObjectFactory pgpF = new PGPObjectFactory(PGPUtil.getDecoderStream(in), new JcaKeyFingerprintCalculator());
        PGPEncryptedDataList enc;
        Object o = pgpF.nextObject();
        //
        // the first object might be a PGP marker packet.
        //
        if (o instanceof PGPEncryptedDataList) {
            enc = (PGPEncryptedDataList) o;
        } else {
            enc = (PGPEncryptedDataList) pgpF.nextObject();
        }
        //
        // find the secret key
        //
        Iterator<?> it = enc.getEncryptedDataObjects();
        PGPPublicKeyEncryptedData pbe = null;
        PGPSecretKeyRingCollection pgpSec = new PGPSecretKeyRingCollection(
                PGPUtil.getDecoderStream(privateKeyStream), new JcaKeyFingerprintCalculator());
        PGPPrivateKey sKey = null;

        while (sKey == null && it.hasNext()) {
            pbe = (PGPPublicKeyEncryptedData) it.next();
            sKey = findSecretKey(pgpSec, pbe.getKeyID(), password.toCharArray());
        }
        if (Objects.isNull(sKey)) {
            throw new IllegalArgumentException("fail to find secret key");
        }
        PublicKeyDataDecryptorFactory b = new JcePublicKeyDataDecryptorFactoryBuilder()
                .setProvider(new BouncyCastleProvider()).setContentProvider(new BouncyCastleProvider()).build(sKey);

        InputStream clear = pbe.getDataStream(b);

        PGPObjectFactory plainFact = new PGPObjectFactory(clear, new JcaKeyFingerprintCalculator());

        Object message = plainFact.nextObject();

        PGPOnePassSignatureList onePassSignatureList = null;
        PGPSignatureList signatureList = null;
        PGPCompressedData compressedData;
        ByteArrayOutputStream actualOutput = new ByteArrayOutputStream();
        while (message != null) {
            if (message instanceof PGPCompressedData) {
                compressedData = (PGPCompressedData) message;
                plainFact = new PGPObjectFactory(compressedData.getDataStream(), new JcaKeyFingerprintCalculator());
                message = plainFact.nextObject();
            }

            if (message instanceof PGPLiteralData) {
                // have to read it and keep it somewhere.
                Streams.pipeAll(((PGPLiteralData) message).getInputStream(), actualOutput);
            } else if (message instanceof PGPOnePassSignatureList) {
                onePassSignatureList = (PGPOnePassSignatureList) message;
            } else if (message instanceof PGPSignatureList) {
                signatureList = (PGPSignatureList) message;
            } else {
                throw new PGPException("message unknown message type.");
            }
            message = plainFact.nextObject();
        }
        actualOutput.close();
        byte[] output = actualOutput.toByteArray();
        PGPPublicKey publicKeyIn = null;
        if (onePassSignatureList == null || signatureList == null) {
            throw new PGPException("Poor PGP. Signatures not found.");
        } else {
            for (int i = 0; i < onePassSignatureList.size(); i++) {
                PGPOnePassSignature ops = onePassSignatureList.get(0);
                PGPPublicKeyRingCollection pgpPub = new PGPPublicKeyRingCollection(PGPUtil.getDecoderStream(publicKeyStream), new JcaKeyFingerprintCalculator());
                PGPSignature signature = signatureList.get(i);
                publicKeyIn = pgpPub.getPublicKey(signature.getKeyID());
                if (publicKeyIn != null) {
                    ops.init(new JcaPGPContentVerifierBuilderProvider().setProvider(BOUNCY_CASTLE_PROVIDER), publicKeyIn);
                    ops.update(output);
                    if (ops.verify(signature)) {
                        Iterator<?> userIds = publicKeyIn.getUserIDs();
                        while (userIds.hasNext()) {
                            String userId = (String) userIds.next();
                            LOGGER.debug(userId);
                        }
                    } else {
                        throw new SignatureException("Signature verification failed");
                    }
                }
            }
        }

        if (pbe.isIntegrityProtected() && !pbe.verify()) {
            throw new PGPException("Data is integrity protected but integrity is lost.");
        } else if (publicKeyIn == null) {
            throw new SignatureException("Signature not found");
        } else {
            out.write(output);
            out.flush();
            out.close();
        }
    }
}
