package com.zhangry.demo.security.rest.token;

import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Created by zhangry on 2017/3/28.
 */
public class DefaultTokenManager implements TokenManager {
    private Map<String, TokenInfo> validTokens = new HashMap();
    private Map<String, TokenInfo> validUsers = new HashMap();

    public DefaultTokenManager() {
    }

    public TokenInfo createNewToken(UserDetails userDetails) {
        if(userDetails == null) {
            return null;
        } else {
            String token = TokenUtils.generateToken();
            TokenInfo newTokenInfo = new TokenInfo(userDetails, userDetails.getUsername(), token);
            return newTokenInfo;
        }
    }

    public void setCache(TokenInfo tokenInfo) {
        if(tokenInfo != null) {
            this.setTokenCache(tokenInfo.getToken(), tokenInfo);
            this.setUserCache(tokenInfo.getUsername(), tokenInfo);
        }

    }

    public void setTokenCache(String token, TokenInfo tokenInfo) {
        this.validTokens.put(token, tokenInfo);
    }

    public void setUserCache(String username, TokenInfo tokenInfo) {
        this.validUsers.put(username, tokenInfo);
    }

    public TokenInfo getTokenInfoByToken(String token) {
        return (TokenInfo)this.validTokens.get(token);
    }

    public TokenInfo getTokenInfoByUser(String username) {
        return (TokenInfo)this.validUsers.get(username);
    }

    public void removeCache(TokenInfo tokenInfo) {
        if(tokenInfo != null) {
            this.removeTokenCache(tokenInfo.getToken());
            this.removeUserCache(tokenInfo.getUsername());
        }

    }

    public void removeTokenCache(String token) {
        this.validTokens.remove(token);
    }

    public void removeUserCache(String username) {
        this.validUsers.remove(username);
    }

    public Map<String, TokenInfo> getValidTokens() {
        return this.validTokens;
    }

    public void setValidTokens(Map<String, TokenInfo> validTokens) {
        this.validTokens = validTokens;
    }

    public Map<String, TokenInfo> getValidUsers() {
        return this.validUsers;
    }

    public void setValidUsers(Map<String, TokenInfo> validUsers) {
        this.validUsers = validUsers;
    }
}

