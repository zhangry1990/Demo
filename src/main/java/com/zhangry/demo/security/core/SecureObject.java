package com.zhangry.demo.security.core;

/**
 * Created by zhangry on 2017/3/27.
 */
public class SecureObject implements ISecureObject {
    private String secureId;

    public SecureObject() {
    }

    public SecureObject(String secureId) {
        this.secureId = secureId;
    }

    public void setSecureId(String secureId) {
        this.secureId = secureId;
    }

    public String getSecureId() {
        return this.secureId;
    }
}
