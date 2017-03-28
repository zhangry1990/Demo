package com.zhangry.demo.service;

import com.thinvent.common.exception.ServiceException;

/**
 * Created by zhangry on 2017/3/28.
 */
public interface LogService {
    void recordLog(Exception var1) throws ServiceException;
}

