package com.zhangry.demo.service;

import java.io.Serializable;

/**
 * Created by zhangry on 2017/3/28.
 */
public interface BaseService<T, PK extends Serializable> {
    T get(PK var1);
}