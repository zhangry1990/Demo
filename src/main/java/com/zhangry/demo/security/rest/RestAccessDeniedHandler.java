package com.zhangry.demo.security.rest;

import com.thinvent.common.util.HttpUtil;
import com.thinvent.security.rest.response.Printer;
import com.thinvent.service.LogService;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * Created by zhangry on 2017/3/28.
 */
public class RestAccessDeniedHandler implements AccessDeniedHandler {
    private static Logger logger = LoggerFactory.getLogger(RestAccessDeniedHandler.class);
    private LogService logService;

    public RestAccessDeniedHandler() {
    }

    public LogService getLogService() {
        return this.logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public void handle(final HttpServletRequest request, HttpServletResponse response, final AccessDeniedException ex) throws IOException, ServletException {
        if (ex != null && this.logService != null) {
            (new Thread() {
                public void run() {
                    String exceptionMessage = String.format("%s ---> %s", new Object[]{HttpUtil.getCurrentUrl(request), "未授权"});
                    RestAccessDeniedHandler.this.logService.recordLog(new AccessDeniedException(exceptionMessage, ex));
                }
            }).start();
        }

        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(403);
        Printer.printMessage(response, "未授权");
    }
}