using Org.BouncyCastle.Bcpg;
using Org.BouncyCastle.Bcpg.OpenPgp;
using Org.BouncyCastle.Security;
using Org.BouncyCastle.Utilities.IO;

namespace Utility
{
    public class OpenPGPHelper
    {
        private static NLog.Logger logger = NLog.LogManager.GetCurrentClassLogger();
        private static OpenPGPHelper? instance;
        private OpenPGPHelper()
        {

        }

        public static OpenPGPHelper GetInstance()
        {
            if (instance == null)
            {
                instance = new OpenPGPHelper();
            }
            return instance;
        }

        public PgpPublicKey ReadPublickey(Stream inputStream)
        {
            PgpPublicKeyRingBundle pubRings = new PgpPublicKeyRingBundle(PgpUtilities.GetDecoderStream(inputStream));
            PgpPublicKey key = null;

            foreach (PgpPublicKeyRing kRing in pubRings.GetKeyRings())
            {
                foreach (PgpPublicKey k in kRing.GetPublicKeys())
                {
                    logger.Debug("Is Encryption Key =" + k.IsEncryptionKey);
                    logger.Debug("Is Master Key =" + k.IsMasterKey);
                    logger.Debug("Is Revoked Key =" + k.IsRevoked());
                    logger.Debug("Key ID =" + k.KeyId);
                    logger.Debug("Key Strength =" + k.BitStrength);
                    if (k.IsEncryptionKey)
                    {
                        key = k;
                        break;
                    }
                }
                if (key != null)
                {
                    break;
                }
            }
            if (key == null)
            {
                throw new System.Exception("Can't find encryption key in key ring.");
            }

            return key;
        }

        public PgpPrivateKey FindSecretKey(PgpSecretKey pgpSecretKey, char[] pass)
        {
            return pgpSecretKey.ExtractPrivateKey(pass);
        }

        public static PgpPrivateKey FindSecretKey(PgpSecretKeyRingBundle pgpSec, long keyID, char[] pass)
        {
            PgpSecretKey pgpSecKey = pgpSec.GetSecretKey(keyID);

            if (pgpSecKey == null)
            {
                return null;
            }

            return pgpSecKey.ExtractPrivateKey(pass);
        }

        public PgpSecretKey ReadSecretKey(Stream inputStream)
        {
            PgpSecretKeyRingBundle pgpSec = new PgpSecretKeyRingBundle(PgpUtilities.GetDecoderStream(inputStream));

            // To Be Improved
            // we just loop through the collection till we find a key suitable for encryption, in the real
            // world you would probably want to be a bit smarter about this.

            foreach (var keyRing in pgpSec.GetKeyRings())
            {
                foreach (var key in keyRing.GetSecretKeys())
                {
                    if (key.IsSigningKey)
                    {
                        return key;
                    }
                }
            }
            throw new System.Exception("Can't find signing key in key ring.");
        }

        public void EncryptAndSign(Stream outputStream, Stream inputStream, PgpPublicKey encryptionKey, PgpPrivateKey signingKey, string identify)
        {
            outputStream = new ArmoredOutputStream(outputStream);
            try
            {
                PgpEncryptedDataGenerator encryptedDataGenerator = new PgpEncryptedDataGenerator(SymmetricKeyAlgorithmTag.Aes256, true, new SecureRandom());
                encryptedDataGenerator.AddMethod(encryptionKey);

                Stream encryptedOut = encryptedDataGenerator.Open(outputStream, new byte[1 << 16]);

                PgpCompressedDataGenerator compressedDataGenerator = new PgpCompressedDataGenerator(CompressionAlgorithmTag.Zip);
                Stream compressedOut = compressedDataGenerator.Open(encryptedOut);

                PgpSignatureGenerator signatureGenerator = new PgpSignatureGenerator(signingKey.PublicKeyPacket.Algorithm, HashAlgorithmTag.Sha512);
                signatureGenerator.InitSign(PgpSignature.BinaryDocument, signingKey);
                foreach (string userId in encryptionKey.GetUserIds())
                {
                    PgpSignatureSubpacketGenerator subpacketGenerator = new PgpSignatureSubpacketGenerator();
                    subpacketGenerator.SetSignerUserId(false, userId);
                    signatureGenerator.SetHashedSubpackets(subpacketGenerator.Generate());
                    break;
                }
                signatureGenerator.GenerateOnePassVersion(false).Encode(compressedOut);

                PgpLiteralDataGenerator literalDataGenerator = new PgpLiteralDataGenerator();
                Stream literalOut = literalDataGenerator.Open(compressedOut, PgpLiteralData.Binary, identify, DateTime.Now, new byte[1 << 16]);

                byte[] buffer = new byte[1 << 16];
                int bytesRead;
                while ((bytesRead = inputStream.Read(buffer, 0, buffer.Length)) > 0)
                {
                    literalOut.Write(buffer, 0, bytesRead);
                    signatureGenerator.Update(buffer, 0, bytesRead);
                }

                literalOut.Close();
                literalDataGenerator.Close();

                signatureGenerator.Generate().Encode(compressedOut);

                compressedOut.Close();
                compressedDataGenerator.Close();

                encryptedOut.Close();
                encryptedDataGenerator.Close();

                outputStream.Close();
            }
            catch (PgpException e)
            {
                if (e.InnerException != null)
                {
                    logger.Error(e.InnerException.Message, e.InnerException);
                }
                else
                {
                    logger.Error(e.Message, e);
                }
            }
            catch (Exception e)
            {
                logger.Error(e.Message, e);
            }
        }

        public void DecryptAndVerify(Stream inputStream, Stream outputStream, Stream publicKeyStream, Stream privateKeyStream, string password)
        {
            PgpObjectFactory pgpF = new PgpObjectFactory(PgpUtilities.GetDecoderStream(inputStream));
            // PgpEncryptedDataList enc = pgpF.NextPgpObject() as PgpEncryptedDataList;

            PgpEncryptedDataList enc;
            Object o = pgpF.NextPgpObject();
            if(o.GetType() == typeof(PgpEncryptedDataList))
            {
                enc = (PgpEncryptedDataList) o;
            }
            else
            {
                enc = (PgpEncryptedDataList) pgpF.NextPgpObject();
            }

            PgpPublicKeyEncryptedData pbe = null;
            PgpSecretKeyRingBundle pgpSec = new PgpSecretKeyRingBundle(PgpUtilities.GetDecoderStream(privateKeyStream));
            PgpPrivateKey sKey = null;
            foreach (PgpPublicKeyEncryptedData data in enc.GetEncryptedDataObjects())
            {
                pbe = data;
                sKey = FindSecretKey(pgpSec, pbe.KeyId, password.ToCharArray());
            }
            if(sKey == null)
            {
                throw new System.Exception("fail to find secret key");
            }

            Stream clear = pbe.GetDataStream(sKey);
            PgpObjectFactory plainFact = new PgpObjectFactory(clear);
            object message = plainFact.NextPgpObject();
            PgpOnePassSignatureList onePassSignatureList = null;
            PgpSignatureList signatureList = null;
            PgpCompressedData compressedData;
            MemoryStream actualOutput = new MemoryStream();

            while (message != null)
            {
                if (message is PgpCompressedData)
                {
                    compressedData = (PgpCompressedData)message;
                    plainFact = new PgpObjectFactory(compressedData.GetDataStream());
                    message = plainFact.NextPgpObject();
                }

                if (message is PgpLiteralData)
                {
                    // have to read it and keep it somewhere.
                    Streams.PipeAll(((PgpLiteralData)message).GetInputStream(), actualOutput);
                }
                else if (message is PgpOnePassSignatureList)
                {
                    onePassSignatureList = (PgpOnePassSignatureList)message;
                }
                else if (message is PgpSignatureList)
                {
                    signatureList = (PgpSignatureList)message;
                }
                else
                {
                    throw new PgpException("message unknown message type.");
                }

                message = plainFact.NextPgpObject();
            }

            actualOutput.Close();
            byte[] output = actualOutput.ToArray();
            PgpPublicKey publicKeyIn = null;

            if (onePassSignatureList == null || signatureList == null)
            {
                throw new PgpException("Poor PGP. Signatures not found.");
            }
            else
            {
                PgpPublicKeyRingBundle pgpPub = new PgpPublicKeyRingBundle(PgpUtilities.GetDecoderStream(publicKeyStream));
                for (int i = 0; i < onePassSignatureList.Count; i++)
                {
                    PgpOnePassSignature ops = onePassSignatureList[0];
                    PgpSignature signature = signatureList[i];
                    publicKeyIn = pgpPub.GetPublicKey(signature.KeyId);

                    if (publicKeyIn != null)
                    {
                        ops.InitVerify(publicKeyIn);
                        ops.Update(output);
                        if (ops.Verify(signature))
                        {
                            logger.Debug(string.Join(",", publicKeyIn.GetUserIds()));
                        }
                        else
                        {
                            throw new SignatureException("Signature verification failed");
                        }
                    }
                }
            }

            if (pbe.IsIntegrityProtected() && !pbe.Verify())
            {
                throw new PgpException("Data is integrity protected but integrity is lost.");
            }
            else if (publicKeyIn == null)
            {
                throw new SignatureException("Signature not found");
            }
            else
            {
                outputStream.Write(output, 0, output.Length);
            }
        }
    }
}