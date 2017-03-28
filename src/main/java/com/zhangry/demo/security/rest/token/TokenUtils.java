package com.zhangry.demo.security.rest.token;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import org.springframework.security.crypto.codec.Base64;

/**
 * Created by zhangry on 2017/3/28.
 */
public class TokenUtils {
    public TokenUtils() {
    }

    public static String generateToken() {
        byte[] tokenBytes = new byte[32];
        (new SecureRandom()).nextBytes(tokenBytes);
        return new String(Base64.encode(tokenBytes), StandardCharsets.UTF_8);
    }
}

