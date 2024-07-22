package com.dattran.identity_service.domain.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OtpService {
    RedisTemplate<String, Object> redisTemplate;
    @NonFinal
    @Value("${otp.expiration}")
    long OTP_EXPIRATION_MINUTES;

    public String generateOTP(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }

    public void storeOtp(String accountId, String otpCode) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        ops.set(accountId, otpCode, OTP_EXPIRATION_MINUTES, TimeUnit.MINUTES);
    }

    public boolean isOtpExpired(String accountId) {
        Long expireTime = redisTemplate.getExpire(accountId, TimeUnit.SECONDS);
        return (expireTime == null || expireTime <= 0);
    }

    public String getOtp(String accountId) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        return (String) ops.get(accountId);
    }

    public void deleteOtp(String accountId) {
        redisTemplate.delete(accountId);
    }

    public boolean validateOtp(String accountId, String otpCode) {
        String storedOtp = getOtp(accountId);
        if (storedOtp != null && storedOtp.equals(otpCode)) {
            deleteOtp(accountId);
            return true;
        }
        return false;
    }
}
