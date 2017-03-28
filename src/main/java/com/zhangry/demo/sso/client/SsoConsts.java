package com.zhangry.demo.sso.client;

/**
 * Created by zhangry on 2017/3/28.
 */
public interface SsoConsts {
    String SSO_SERVICE = "/sso-service";
    String TICKET_PARAMETER = "ticket";
    String X_FORWARDED_FOR = "X-Forwarded-For";
    String SSO_ASSERTION_KEY = "__CONST_SSO_ASSERTION__";
    String ORIGINAL_URL_KEY = "__CONST_ORIGINAL_URL__";
    String KOAL_LOGIN_URL = "koal-login-url";
    String SSO_SERVER_URL = "sso-server-url";
    String IGNORE_PATH_LIST = "ignore-path-list";
    String LOGIN_FAILED_PAGE = "login-failed-page";
    String LOGIN_SUCCESS_PAGE = "login-success-page";
    String SSO_CALLBACK_CLASS = "sso-callback-class";
}
