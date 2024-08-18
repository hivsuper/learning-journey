package org.lxp.weixin.util;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.util.Objects;

@Slf4j
public class AesCbcUtil {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static String decrypt(String data, String key, String iv, String encodingFormat) {
        //被加密的数据
        final var dataByte = Base64.decode(data);
        //加密秘钥
        final var keyByte = Base64.decode(key);
        //偏移量
        final var ivByte = Base64.decode(iv);
        try {
            final var cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            final var spec = new SecretKeySpec(keyByte, "AES");
            final var parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
            final var resultByte = cipher.doFinal(dataByte);
            if (Objects.nonNull(resultByte) && resultByte.length > 0) {
                return new String(resultByte, encodingFormat);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}