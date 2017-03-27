package com.zhangry.demo.common.util;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * Created by zhangry on 2017/3/17.
 */
public class IdentifyUtil {
    private static SecureRandom random = new SecureRandom();

    private IdentifyUtil() {
    }

    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String uuid2() {
        return UUID.randomUUID().toString();
    }

    public static long randomLong() {
        return Math.abs(random.nextLong());
    }

    public static String randomBase62(int length) {
        byte[] randomBytes = new byte[length];
        random.nextBytes(randomBytes);
        return EncodeUtil.encodeBase62(randomBytes);
    }
}
