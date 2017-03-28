package com.zhangry.demo.security.core;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zhangry on 2017/3/27.
 */
public interface ISecureRequest extends ISecureObject {
    HttpServletRequest getRequest();
}
