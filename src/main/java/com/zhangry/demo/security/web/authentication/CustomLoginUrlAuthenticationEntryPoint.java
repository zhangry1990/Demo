package com.zhangry.demo.security.web.authentication;

import com.thinvent.common.util.HttpUtil;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

/**
 * Created by zhangry on 2017/3/28.
 */
public class CustomLoginUrlAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {
    public CustomLoginUrlAuthenticationEntryPoint(String loginFormUrl) {
        super(loginFormUrl);
    }

    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        if(HttpUtil.isAjaxRequest(request)) {
            response.setContentType("application/json; charset=UTF-8");
            response.setStatus(401);
            PrintWriter out = response.getWriter();
            out.print(authException.getMessage());
        } else {
            super.commence(request, response, authException);
        }

    }
}
