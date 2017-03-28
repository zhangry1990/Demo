package com.zhangry.demo.security.core;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Created by zhangry on 2017/3/27.
 */
public interface IUserManager extends ISecurityManager {
    UserDetails loadUserByUsername(String var1);
}

