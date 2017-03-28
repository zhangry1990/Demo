package com.zhangry.demo.sso.client;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhangry on 2017/3/28.
 */
public interface SsoCallback {
    void postLogin(SsoPrincipal var1, HttpServletRequest var2, HttpServletResponse var3);
}
