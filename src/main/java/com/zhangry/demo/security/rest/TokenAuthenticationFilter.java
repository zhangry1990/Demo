package com.zhangry.demo.security.rest;

import com.thinvent.common.util.HttpUtil;
import com.thinvent.security.rest.auth.AuthenticationHandler;
import com.thinvent.security.rest.response.Printer;
import com.thinvent.security.rest.token.TokenInfo;
import com.thinvent.service.LogService;
import java.io.IOException;
import java.util.StringTokenizer;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Created by zhangry on 2017/3/28.
 */
public final class TokenAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private static Logger logger = LoggerFactory.getLogger(TokenAuthenticationFilter.class);
    private static final String PROCESSING_CONTINUE_FLAG = "_processing_continue";
    private static final String PROCESSING_CONTINUE = "0";
    private static final String PROCESSING_NOT_CONTINUE = "1";
    private final AuthenticationHandler authenticationHandler;
    private final boolean invalidUserPreToken;
    private final String loginUrl;
    private final String logoutUrl;
    private LogService logService;
    private String clientType;

    public TokenAuthenticationFilter(AuthenticationHandler authenticationHandler, boolean invalidUserPreToken, String loginUrl, String logoutUrl) {
        this.authenticationHandler = authenticationHandler;
        this.invalidUserPreToken = invalidUserPreToken;
        this.loginUrl = loginUrl;
        this.logoutUrl = logoutUrl;
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest)servletRequest;
        request.setAttribute("client_type", this.clientType);
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        Authentication authResult = null;
        String currentUrl = HttpUtil.getCurrentUrl(request);
        if(currentUrl.equals(this.logoutUrl)) {
            this.logout(request);
            this.setRequestProcessingFlag(request, "1");
        } else if(currentUrl.equals(this.loginUrl)) {
            authResult = this.login(request, response);
        } else if(!this.checkToken(request, response)) {
            response.setStatus(401);
            response.setHeader("X-MessageKey", "INVALID_TOKEN");
            response.setHeader("X-Message", "非法的访问请求，无效的Token");
            Printer.printMessage(response, "非法的访问请求，无效的Token");
            this.setRequestProcessingFlag(request, "1");
            if(logger.isDebugEnabled()) {
                logger.debug(String.format("*** 无效的Token（%s）", new Object[]{request.getHeader("X-Token")}));
            }

            if(this.logService != null) {
                (new Thread() {
                    public void run() {
                        String exceptionMessage = String.format("%s ---> %s", new Object[]{HttpUtil.getCurrentUrl(request), "非法的访问请求，无效的Token"});
                        TokenAuthenticationFilter.this.logService.recordLog(new Exception(exceptionMessage));
                    }
                }).start();
            }
        }

        if(this.requestProcessingContinue(request)) {
            if(!this.requiresAuthentication(request, response)) {
                chain.doFilter(request, response);
            } else {
                if(authResult == null) {
                    return;
                }

                this.successfulAuthentication(request, response, chain, authResult);
                chain.doFilter(request, response);
            }
        }

    }

    private Authentication login(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        Authentication authentication = this.checkAuthentication(request, response);
        if(authentication != null && authentication.getPrincipal() != null) {
            TokenInfo tokenInfo = this.authenticationHandler.createToken(authentication, this.invalidUserPreToken);
            if(tokenInfo != null) {
                response.setHeader("X-Token", tokenInfo.getToken());
            }

            return authentication;
        } else {
            response.setStatus(401);
            response.setHeader("X-MessageKey", "LOGIN_FAILED");
            response.setHeader("X-Message", "用户身份认证失败");
            Printer.printMessage(response, "用户身份认证失败");
            return authentication;
        }
    }

    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String currentUrl = HttpUtil.getCurrentUrl(request);
        return currentUrl.equals(this.loginUrl);
    }

    private Authentication checkAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        Authentication authentication = null;
        String authorization = request.getHeader("X-Authorization");
        if(StringUtils.isNotBlank(authorization)) {
            authentication = this.checkBasicAuthorization(authorization, request, response);
        } else {
            String username = request.getHeader("X-Username");
            String password = request.getHeader("X-Password");
            if(StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
                authentication = this.checkUsernameAndPassword(username, password, request, response);
            }
        }

        return authentication;
    }

    private Authentication checkBasicAuthorization(String authorization, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        StringTokenizer tokenizer = new StringTokenizer(authorization);
        return tokenizer.countTokens() >= 2?this.checkUsernameAndPassword(tokenizer.nextToken(), tokenizer.nextToken(), request, response):null;
    }

    private Authentication checkUsernameAndPassword(String username, String password, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(username == null) {
            username = "";
        }

        if(password == null) {
            password = "";
        }

        username = username.trim();
        UsernamePasswordAuthenticationToken authentication = null;

        Authentication authentication;
        try {
            authentication = new UsernamePasswordAuthenticationToken(username, password);
            authentication = this.getAuthenticationManager().authenticate(authentication);
        } catch (InternalAuthenticationServiceException var7) {
            this.unsuccessfulAuthentication(request, response, var7);
            return null;
        } catch (AuthenticationException var8) {
            this.unsuccessfulAuthentication(request, response, var8);
            return null;
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    private void logout(HttpServletRequest request) {
        String token = request.getHeader("X-Token");
        this.authenticationHandler.logout(token, this.invalidUserPreToken);
    }

    private boolean checkToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        boolean result = false;
        String token = request.getHeader("X-Token");
        if(StringUtils.isNotBlank(token) && this.authenticationHandler.checkToken(token)) {
            result = true;
            if(logger.isDebugEnabled()) {
                logger.debug(String.format("*** 有效的Token（%s） ---> %s", new Object[]{token, SecurityContextHolder.getContext().getAuthentication().getPrincipal()}));
            }
        }

        return result;
    }

    private void setRequestProcessingFlag(HttpServletRequest request, String flag) {
        request.setAttribute("_processing_continue", flag);
    }

    private boolean requestProcessingContinue(HttpServletRequest request) {
        Object processingContinueFlag = request.getAttribute("_processing_continue");
        return processingContinueFlag == null || String.valueOf(processingContinueFlag).equals("0");
    }

    public LogService getLogService() {
        return this.logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public String getClientType() {
        return this.clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }
}

