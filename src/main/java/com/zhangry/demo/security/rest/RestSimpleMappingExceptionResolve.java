package com.zhangry.demo.security.rest;

import com.thinvent.service.LogService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

/**
 * Created by zhangry on 2017/3/28.
 */
public class RestSimpleMappingExceptionResolve extends SimpleMappingExceptionResolver {
    private static Logger logger = LoggerFactory.getLogger(RestSimpleMappingExceptionResolve.class);
    private LogService logService;

    public RestSimpleMappingExceptionResolve() {
    }

    public LogService getLogService() {
        return this.logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public ModelAndView resolveException(HttpServletRequest httpRequest, HttpServletResponse httpResponse, Object handler, final Exception ex) {
        if(ex != null && this.logService != null) {
            (new Thread() {
                public void run() {
                    RestSimpleMappingExceptionResolve.this.logService.recordLog(ex);
                }
            }).start();
        }

        httpResponse.setContentType("application/json;charset=UTF-8");
        httpResponse.setCharacterEncoding("UTF-8");
        if(ex instanceof NoHandlerFoundException) {
            httpResponse.setStatus(404);
        } else {
            httpResponse.setStatus(500);
        }

        return null;
    }
}