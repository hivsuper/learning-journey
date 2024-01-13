using System.Reflection;
using Org.BouncyCastle.Bcpg.OpenPgp;
using Org.BouncyCastle.Utilities.Encoders;

namespace Utility.Example;

class OpenPGPHelperExample
{
    public static void Main(string[] args)
    {
        var assembly = Assembly.GetExecutingAssembly();
        var identify = "Test User";
        var password = "passphrase";
        string? response = null;
        var payload = "test";

        var privateKeyPath = assembly.GetManifestResourceNames().Single(x=>x.EndsWith("private_key.asc"));
        var publicKeyPath = assembly.GetManifestResourceNames().Single(x=>x.EndsWith("public_key.asc"));

        // Console.WriteLine(privateKeyPath);
        // Console.WriteLine(publicKeyPath);

        OpenPGPHelper openPGPHelper = OpenPGPHelper.GetInstance();
        using (Stream? publicKeyStream = assembly.GetManifestResourceStream(publicKeyPath),
        privateKeyStream = assembly.GetManifestResourceStream(privateKeyPath))
        {
            if (publicKeyStream != null && privateKeyStream != null)
            {
                PgpPublicKey publicKey = openPGPHelper.ReadPublickey(publicKeyStream);
                PgpSecretKey secretKey = openPGPHelper.ReadSecretKey(privateKeyStream);
                PgpPrivateKey privateKey = openPGPHelper.FindSecretKey(secretKey, password.ToCharArray());
                using (MemoryStream outputStream = new MemoryStream(), inputStream = new MemoryStream(System.Text.Encoding.Default.GetBytes(payload)))
                {
                    openPGPHelper.EncryptAndSign(outputStream, inputStream, publicKey, privateKey, identify);
                    response = Base64.ToBase64String(outputStream.ToArray());
                    Console.WriteLine(response);
                }
            }
        }

        using (Stream? publicKeyStream = assembly.GetManifestResourceStream(publicKeyPath),
        privateKeyStream = assembly.GetManifestResourceStream(privateKeyPath))
        {
            if (publicKeyStream != null && privateKeyStream != null)
            {
                using (MemoryStream outputStream = new MemoryStream(), inputStream = new MemoryStream(Base64.Decode(response)))
                {
                    openPGPHelper.DecryptAndVerify(inputStream, outputStream, publicKeyStream, privateKeyStream, password);
                    Console.WriteLine(System.Text.Encoding.UTF8.GetString(outputStream.ToArray()));
                }
            }
        }
    }
}