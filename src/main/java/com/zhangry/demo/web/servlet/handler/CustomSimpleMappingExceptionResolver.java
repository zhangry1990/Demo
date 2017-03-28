package com.zhangry.demo.web.servlet.handler;

import com.thinvent.common.util.HttpUtil;
import com.thinvent.service.LogService;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

/**
 * Created by zhangry on 2017/3/28.
 */
public class CustomSimpleMappingExceptionResolver extends SimpleMappingExceptionResolver {
    private static Logger logger = LoggerFactory.getLogger(CustomSimpleMappingExceptionResolver.class);
    private static final String EXCEPTION = "javax.servlet.error.exception";
    private static final String HTTP_STATUS_CODE = "javax.servlet.error.status_code";
    private String ajaxErrorView;
    private String ajaxDefaultErrorMessage = "AJAX Error!";
    private boolean ajaxShowTechMessage = true;
    private LogService logService;

    public CustomSimpleMappingExceptionResolver() {
    }

    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, final Exception ex) {
        if(ex != null) {
            logger.error(ex.getMessage(), ex);
            if(this.logService != null) {
                (new Thread() {
                    public void run() {
                        CustomSimpleMappingExceptionResolver.this.logService.recordLog(ex);
                    }
                }).start();
            }
        }

        if(HttpUtil.isAjaxRequest(request)) {
            ModelAndView modelView = new ModelAndView(this.ajaxErrorView);
            modelView.addObject("exception", ex);
            response.setStatus(500);
            return modelView;
        } else {
            return super.resolveException(request, response, handler, ex);
        }
    }

    private String getExceptionMessage(Throwable e) {
        List<String> messages = new ArrayList();
        if(e != null) {
            messages.add(e.getClass().toString());
        }

        while(e != null) {
            messages.add(e.getMessage());
            e = e.getCause();
        }

        return StringUtils.join(messages.toArray(), "\n");
    }

    public String getAjaxErrorView() {
        return this.ajaxErrorView;
    }

    public void setAjaxErrorView(String ajaxErrorView) {
        this.ajaxErrorView = ajaxErrorView;
    }

    public String getAjaxDefaultErrorMessage() {
        return this.ajaxDefaultErrorMessage;
    }

    public void setAjaxDefaultErrorMessage(String ajaxDefaultErrorMessage) {
        this.ajaxDefaultErrorMessage = ajaxDefaultErrorMessage;
    }

    public boolean isAjaxShowTechMessage() {
        return this.ajaxShowTechMessage;
    }

    public void setAjaxShowTechMessage(boolean ajaxShowTechMessage) {
        this.ajaxShowTechMessage = ajaxShowTechMessage;
    }

    public LogService getLogService() {
        return this.logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }
}
