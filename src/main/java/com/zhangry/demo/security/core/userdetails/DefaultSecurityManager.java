package com.zhangry.demo.security.core.userdetails;

import com.thinvent.security.core.ISecureObject;
import com.thinvent.security.core.ISecureObjectManager;
import com.thinvent.security.core.IUserManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Created by zhangry on 2017/3/27.
 */
public class DefaultSecurityManager implements IUserManager, ISecureObjectManager {
    private static Map<String, Collection<ConfigAttribute>> metadataSource = null;

    public DefaultSecurityManager() {
    }

    public Map<String, Collection<ConfigAttribute>> loadAllConfigAttributes() {
        if(metadataSource == null) {
            metadataSource = new HashMap();
            Collection<ConfigAttribute> signinCF = new HashSet();
            signinCF.add(new SecurityConfig("IS_AUTHENTICATED_ANONYMOUSLY"));
            metadataSource.put("/signin", signinCF);
            Collection<ConfigAttribute> userCF = new HashSet();
            userCF.add(new SecurityConfig("user"));
            metadataSource.put("/**", userCF);
        }

        return metadataSource;
    }

    public Collection<ConfigAttribute> loadConfigAttributes(ISecureObject secureObject) {
        if(secureObject.getSecureId().equalsIgnoreCase("M01")) {
            List<ConfigAttribute> attributes = new ArrayList();
            attributes.add(new SecurityConfig("user"));
            return attributes;
        } else {
            return null;
        }
    }

    public UserDetails loadUserByUsername(String username) {
        return new User(username, "c4ca4238a0b923820dcc509a6f75849b", true, true, true, true, AuthorityUtils.createAuthorityList(new String[]{"user"}));
    }
}

