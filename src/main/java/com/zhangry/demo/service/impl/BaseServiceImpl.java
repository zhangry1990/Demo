package com.zhangry.demo.service.impl;

import com.thinvent.data.hibernate.BaseDAO;
import com.thinvent.service.BaseService;
import java.io.Serializable;
import java.util.Locale;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

/**
 * Created by zhangry on 2017/3/28.
 */
public class BaseServiceImpl<T, PK extends Serializable> implements BaseService<T, PK> {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private MessageSource messageSource;
    @Autowired
    private BaseDAO<T, PK> baseDAO;

    public BaseServiceImpl() {
    }

    public T get(PK id) {
        return this.baseDAO.get(id);
    }

    protected String getMsg(String key) {
        return this.messageSource.getMessage(key, new Object[0], Locale.getDefault());
    }

    protected String getMsg(String key, String... args) {
        return this.messageSource.getMessage(key, args, Locale.getDefault());
    }
}