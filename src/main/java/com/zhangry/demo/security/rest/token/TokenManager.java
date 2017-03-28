package com.zhangry.demo.security.rest.token;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Created by zhangry on 2017/3/28.
 */
public interface TokenManager {
    TokenInfo createNewToken(UserDetails var1);

    void setCache(TokenInfo var1);

    void setTokenCache(String var1, TokenInfo var2);

    void setUserCache(String var1, TokenInfo var2);

    TokenInfo getTokenInfoByToken(String var1);

    TokenInfo getTokenInfoByUser(String var1);

    void removeCache(TokenInfo var1);

    void removeTokenCache(String var1);

    void removeUserCache(String var1);
}