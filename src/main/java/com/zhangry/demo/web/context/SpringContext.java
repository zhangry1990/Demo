package com.zhangry.demo.web.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by zhangry on 2017/3/28.
 */
public class SpringContext implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    public SpringContext() {
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        applicationContext = applicationContext;
    }

    public static Object getBean(String beanName) {
        return applicationContext.getBean(beanName);
    }
}
