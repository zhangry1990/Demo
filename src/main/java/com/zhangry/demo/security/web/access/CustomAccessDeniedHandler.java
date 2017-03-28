package com.zhangry.demo.security.web.access;

import com.thinvent.common.util.HttpUtil;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.security.web.csrf.MissingCsrfTokenException;

/**
 * Created by zhangry on 2017/3/28.
 */
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private String errorPage = null;
    private String csrfErrorPage = null;

    public CustomAccessDeniedHandler() {
    }

    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        String errPage = null;
        request.setAttribute("exception", accessDeniedException);
        response.setStatus(403);
        if(HttpUtil.isAjaxRequest(request)) {
            response.setContentType("application/json; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.print(accessDeniedException.getMessage());
        } else {
            if(!(accessDeniedException instanceof MissingCsrfTokenException) && !(accessDeniedException instanceof InvalidCsrfTokenException)) {
                if(!response.isCommitted()) {
                    errPage = this.errorPage;
                }
            } else {
                errPage = this.csrfErrorPage;
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher(errPage);
            dispatcher.forward(request, response);
        }

    }

    public void setErrorPage(String errorPage) {
        if(!StringUtils.isBlank(errorPage) && errorPage.startsWith("/")) {
            this.errorPage = errorPage;
        } else {
            throw new IllegalArgumentException("error page must begin with '/'");
        }
    }

    public void setCsrfErrorPage(String csrfErrorPage) {
        if(!StringUtils.isBlank(csrfErrorPage) && csrfErrorPage.startsWith("/")) {
            this.csrfErrorPage = csrfErrorPage;
        } else {
            throw new IllegalArgumentException("csrf error page must begin with '/'");
        }
    }
}
