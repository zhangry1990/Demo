package com.zhangry.demo.security.rest.token;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Created by zhangry on 2017/3/28.
 */
public class TokenManagerForEhcache implements TokenManager {
    @Autowired
    private CacheManager cacheManager;
    private String tokenCacheName;
    private String userCacheName;
    private Cache tokenCache;
    private Cache userCache;

    public TokenManagerForEhcache() {
    }

    public TokenInfo createNewToken(UserDetails userDetails) {
        if(userDetails == null) {
            return null;
        } else {
            String token = TokenUtils.generateToken();
            TokenInfo tokenInfo = new TokenInfo(userDetails, userDetails.getUsername(), token);
            return tokenInfo;
        }
    }

    public void setCache(TokenInfo tokenInfo) {
        if(tokenInfo != null) {
            this.setTokenCache(tokenInfo.getToken(), tokenInfo);
            this.setUserCache(tokenInfo.getUsername(), tokenInfo);
        }

    }

    public void setTokenCache(String token, TokenInfo tokenInfo) {
        this.getTokenCache().put(new Element(token, tokenInfo));
    }

    public void setUserCache(String username, TokenInfo tokenInfo) {
        this.getUserCache().put(new Element(username, tokenInfo));
    }

    public TokenInfo getTokenInfoByToken(String token) {
        Element element = this.getTokenCache().get(token);
        return element != null?(TokenInfo)element.getObjectValue():null;
    }

    public TokenInfo getTokenInfoByUser(String username) {
        Element element = this.getUserCache().get(username);
        return element != null?(TokenInfo)element.getObjectValue():null;
    }

    public void removeCache(TokenInfo tokenInfo) {
        if(tokenInfo != null) {
            this.removeTokenCache(tokenInfo.getToken());
            this.removeUserCache(tokenInfo.getUsername());
        }

    }

    public void removeTokenCache(String token) {
        this.getTokenCache().remove(token);
    }

    public void removeUserCache(String username) {
        this.getUserCache().remove(username);
    }

    public Cache getTokenCache() {
        if(this.tokenCache == null) {
            this.tokenCache = this.cacheManager.getCache(this.tokenCacheName);
        }

        return this.tokenCache;
    }

    public void setTokenCache(Cache tokenCache) {
        this.tokenCache = tokenCache;
    }

    public Cache getUserCache() {
        if(this.userCache == null) {
            this.userCache = this.cacheManager.getCache(this.userCacheName);
        }

        return this.userCache;
    }

    public void setUserCache(Cache userCache) {
        this.userCache = userCache;
    }

    public String getTokenCacheName() {
        return this.tokenCacheName;
    }

    public void setTokenCacheName(String tokenCacheName) {
        this.tokenCacheName = tokenCacheName;
    }

    public String getUserCacheName() {
        return this.userCacheName;
    }

    public void setUserCacheName(String userCacheName) {
        this.userCacheName = userCacheName;
    }
}

