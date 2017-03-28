package com.zhangry.demo.security.rest.auth;

import com.thinvent.security.rest.token.TokenInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Created by zhangry on 2017/3/28.
 */
public interface AuthenticationHandler {
    TokenInfo createToken(Authentication var1, boolean var2);

    boolean checkToken(String var1);

    void logout(String var1, boolean var2);

    UserDetails getCurrentUser();
}
