package com.zhangry.demo.common.util;

import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Map;

/**
 * Created by zhangry on 2017/3/17.
 */
public class AssertUtil {
    private AssertUtil() {
    }

    public static void notNull(Object o) {
        Assert.notNull(o);
    }

    public static void notNull(Object o, String msg) {
        Assert.notNull(o, msg);
    }

    public static void notEmpty(String s) {
        if(StringUtil.isNullOrEmpty(s)) {
            throw new IllegalArgumentException("[Assertion failed] - this arg must not be empty");
        }
    }

    public static void notEmpty(String s, String msg) {
        if(StringUtil.isNullOrEmpty(s)) {
            throw new IllegalArgumentException(msg);
        }
    }

    public static void notEmpty(Collection<?> collection) {
        Assert.notEmpty(collection);
    }

    public static void notEmpty(Collection<?> collection, String msg) {
        Assert.notEmpty(collection, msg);
    }

    public static void notEmpty(Map<?, ?> map) {
        Assert.notEmpty(map);
    }

    public static void notEmpty(Map<?, ?> map, String msg) {
        Assert.notEmpty(map, msg);
    }

    public static void notEmpty(Object[] array) {
        Assert.notEmpty(array);
    }

    public static void notEmpty(Object[] array, String msg) {
        Assert.notEmpty(array, msg);
    }

    public static void isTrue(boolean expression) {
        isTrue(expression, "[Assertion failed] - this expression must be true");
    }

    public static void isTrue(boolean expression, String message) {
        if(!expression) {
            throw new IllegalArgumentException(message);
        }
    }
}

