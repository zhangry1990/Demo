package com.zhangry.demo.security.rest.token;

/**
 * Created by zhangry on 2017/3/28.
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.security.core.userdetails.UserDetails;

public class TokenManagerForSimpleCache implements TokenManager {
    @Autowired
    private CacheManager cacheManager;
    private String tokenCacheName;
    private String userCacheName;
    private Cache tokenCache;
    private Cache userCache;

    public TokenManagerForSimpleCache() {
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
        this.getTokenCache().put(token, tokenInfo);
    }

    public void setUserCache(String username, TokenInfo tokenInfo) {
        this.getUserCache().put(username, tokenInfo);
    }

    public TokenInfo getTokenInfoByToken(String token) {
        ValueWrapper element = this.getTokenCache().get(token);
        return element != null?(TokenInfo)element.get():null;
    }

    public TokenInfo getTokenInfoByUser(String username) {
        ValueWrapper element = this.getUserCache().get(username);
        return element != null?(TokenInfo)element.get():null;
    }

    public void removeCache(TokenInfo tokenInfo) {
        if(tokenInfo != null) {
            this.removeTokenCache(tokenInfo.getToken());
            this.removeUserCache(tokenInfo.getUsername());
        }

    }

    public void removeTokenCache(String token) {
        this.getTokenCache().evict(token);
    }

    public void removeUserCache(String username) {
        this.getUserCache().evict(username);
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

