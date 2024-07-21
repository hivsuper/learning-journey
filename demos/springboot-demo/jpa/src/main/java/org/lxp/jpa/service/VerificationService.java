package org.lxp.jpa.service;

import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.base.Captcha;
import lombok.RequiredArgsConstructor;
import org.lxp.jpa.dto.VerificationCodeDto;
import org.lxp.jpa.dto.VerificationDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class VerificationService {
    @Value("${verification-code.expired:180}")
    private int timeout;
    private final StringRedisTemplate redisTemplate;

    public VerificationCodeDto generate() {
        Captcha captcha = new ArithmeticCaptcha();
        String verificationCodeKey = UUID.randomUUID().toString();
        String verificationCode = captcha.text();
        VerificationCodeDto verificationCodeDto = new VerificationCodeDto(verificationCodeKey, captcha.toBase64(), verificationCode);
        redisTemplate.opsForValue().set(verificationCodeKey, verificationCode, timeout, TimeUnit.SECONDS);
        return verificationCodeDto;
    }

    public void verify(VerificationDto verificationDto) {
        String inputCode = verificationDto.getVerifyCode();
        if (Optional.ofNullable(
                        redisTemplate.getExpire(verificationDto.getVerifyCodeKey(), TimeUnit.SECONDS))
                .orElse(-1L) < 0) {
            throw new RuntimeException("Verification code is expired");
        }
        String verificationCode = redisTemplate.opsForValue().get(verificationDto.getVerifyCodeKey());
        redisTemplate.delete(verificationDto.getVerifyCodeKey());
        Assert.notNull(verificationCode, "verificationCode should never be null");
        if (!verificationCode.equalsIgnoreCase(inputCode)) {
            throw new RuntimeException("Verification code unmatched");
        }
    }
}
