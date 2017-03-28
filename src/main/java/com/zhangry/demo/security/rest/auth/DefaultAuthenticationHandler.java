package com.zhangry.demo.security.rest.auth;

import com.thinvent.security.rest.token.TokenInfo;
import com.thinvent.security.rest.token.TokenManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

/**
 * Created by zhangry on 2017/3/28.
 */
public class DefaultAuthenticationHandler implements AuthenticationHandler {
    private static Logger logger = LoggerFactory.getLogger(DefaultAuthenticationHandler.class);
    private final AuthenticationManager authenticationManager;
    private final TokenManager tokenManager;

    public DefaultAuthenticationHandler(AuthenticationManager authenticationManager, TokenManager tokenManager) {
        this.authenticationManager = authenticationManager;
        this.tokenManager = tokenManager;
    }

    public TokenInfo createToken(Authentication authentication, boolean invalidUserPreToken) {
        if(authentication != null && authentication.getPrincipal() != null) {
            UserDetails userDetails = (UserDetails)authentication.getPrincipal();
            if(userDetails == null) {
                return null;
            } else {
                TokenInfo newTokenInfo;
                if(invalidUserPreToken) {
                    newTokenInfo = this.tokenManager.getTokenInfoByUser(userDetails.getUsername());
                    this.tokenManager.removeCache(newTokenInfo);
                }

                newTokenInfo = this.tokenManager.createNewToken(userDetails);
                this.tokenManager.setCache(newTokenInfo);
                return newTokenInfo;
            }
        } else {
            return null;
        }
    }

    public boolean checkToken(String token) {
        TokenInfo tokenInfo = this.tokenManager.getTokenInfoByToken(token);
        if(tokenInfo == null) {
            return false;
        } else {
            UserDetails userDetails = tokenInfo.getUserDetails();
            if(userDetails == null) {
                return false;
            } else {
                Authentication paAuthentication = new PreAuthenticatedAuthenticationToken(userDetails, (Object)null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(paAuthentication);
                return true;
            }
        }
    }

    public void logout(String token, boolean invalidUserPreToken) {
        SecurityContextHolder.clearContext();
        if(!StringUtils.isBlank(token)) {
            if(invalidUserPreToken) {
                TokenInfo oldTokenInfo = this.tokenManager.getTokenInfoByToken(token);
                this.tokenManager.removeCache(this.tokenManager.getTokenInfoByToken(token));
                if(logger.isDebugEnabled() && oldTokenInfo != null) {
                    logger.debug("*** 用户退出 ---> " + oldTokenInfo.getUsername());
                }
            }

        }
    }

    public UserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) {
            return null;
        } else {
            Object principal = authentication.getPrincipal();
            return principal == null?null:(UserDetails)principal;
        }
    }
}

