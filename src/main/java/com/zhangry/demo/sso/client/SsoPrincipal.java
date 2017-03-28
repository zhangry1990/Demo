package com.zhangry.demo.sso.client;

import java.io.Serializable;
import java.security.Principal;
import org.jasig.cas.client.authentication.AttributePrincipal;

/**
 * Created by zhangry on 2017/3/28.
 */
public final class SsoPrincipal implements Principal, Serializable {
    private static final long serialVersionUID = 5025079200595208678L;
    private static final String ID_KEY = "ID";
    private static final String NAME_KEY = "NAME";
    private static final String DN_KEY = "DN";
    private static final String LOGINNAME_KEY = "LOGINNAME";
    private final AttributePrincipal attributePrincipal;

    public SsoPrincipal(AttributePrincipal attributePrincipal) {
        this.attributePrincipal = attributePrincipal;
    }

    public String getName() {
        return this.attributePrincipal.getName();
    }

    public int hashCode() {
        return this.attributePrincipal.hashCode();
    }

    public boolean equals(Object obj) {
        return this.attributePrincipal.equals(obj);
    }

    public String toString() {
        return this.attributePrincipal.toString();
    }

    private Object getAttribute(String key) {
        return this.attributePrincipal.getAttributes().get(key);
    }

    public String getLoginName() {
        return (String)this.getAttribute("LOGINNAME");
    }

    public String getUserId() {
        return (String)this.getAttribute("ID");
    }

    public String getUserName() {
        return (String)this.getAttribute("NAME");
    }

    public String getUserDn() {
        return (String)this.getAttribute("DN");
    }
}