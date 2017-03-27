package com.zhangry.demo.common.util;

import java.util.Locale;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by zhangry on 2017/3/17.
 */
public class SpringContextUtil implements ApplicationContextAware {
    private static ApplicationContext ctx;

    private SpringContextUtil() {
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
    }

    public static <T> T getBean(Class<T> clazz) {
        return ctx.getBean(clazz);
    }

    public static <T> T getBean(String beanId, Class<T> clazz) {
        return ctx.getBean(beanId, clazz);
    }

    public static Object getBean(String beanId) {
        return ctx.getBean(beanId);
    }

    public static String getMsg(String key) {
        return ctx.getMessage(key, (Object[])null, Locale.getDefault());
    }

    public static String getMsg(String key, Object... args) {
        return ctx.getMessage(key, args, Locale.getDefault());
    }
}