package com.zhangry.demo.service.cache;

import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;

/**
 * Created by zhangry on 2017/3/28.
 */
public class CacheKeyGenerator implements KeyGenerator {
    private static final Logger log = LoggerFactory.getLogger(CacheKeyGenerator.class);

    public CacheKeyGenerator() {
    }

    public Object generate(Object target, Method method, Object... params) {
        StringBuilder key = new StringBuilder();
        key.append(target.getClass().getSimpleName()).append(".").append(method.getName()).append(":");
        key.append(SimpleKeyGenerator.generateKey(params));
        log.debug("CacheKeyGenerator: key = " + key);
        return key.toString();
    }
}
