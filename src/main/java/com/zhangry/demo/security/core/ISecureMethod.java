package com.zhangry.demo.security.core;

import java.lang.reflect.Method;

/**
 * Created by zhangry on 2017/3/27.
 */
public interface ISecureMethod extends ISecureObject {
    Method getMethod();
}
