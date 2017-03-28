package com.zhangry.demo.sso.client.internal;

import java.security.Principal;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by zhangry on 2017/3/28.
 */
public class HttpServletRequestWrapper extends javax.servlet.http.HttpServletRequestWrapper {
    private final Principal principal;

    public HttpServletRequestWrapper(HttpServletRequest request, Principal principal) {
        super(request);
        this.principal = principal;
    }

    public String getRemoteUser() {
        return this.principal != null?this.principal.getName():super.getRemoteUser();
    }

    public Principal getUserPrincipal() {
        return this.principal != null?this.principal:super.getUserPrincipal();
    }
}
