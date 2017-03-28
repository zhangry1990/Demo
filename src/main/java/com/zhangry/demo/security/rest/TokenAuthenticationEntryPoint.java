package com.zhangry.demo.security.rest;

import com.thinvent.security.rest.response.Printer;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * Created by zhangry on 2017/3/28.
 */
public final class TokenAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static Logger logger = LoggerFactory.getLogger(TokenAuthenticationEntryPoint.class);

    public TokenAuthenticationEntryPoint() {
    }

    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(401);
        response.setHeader("X-MessageKey", "UNAUTHORIZED");
        response.setHeader("X-Message", "未授权");
        Printer.printMessage(response, "未授权");
        if(logger.isDebugEnabled()) {
            logger.debug("*** 未授权 ---> " + request.getRequestURI());
        }

    }
}
