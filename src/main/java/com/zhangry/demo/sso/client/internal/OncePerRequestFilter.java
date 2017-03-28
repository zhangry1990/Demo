package com.zhangry.demo.sso.client.internal;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhangry on 2017/3/28.
 */
public abstract class OncePerRequestFilter implements Filter {
    public static final String ALREADY_FILTERED_SUFFIX = ".FILTERED";
    protected FilterConfig filterConfig;

    public OncePerRequestFilter() {
    }

    protected String getInitParam(String paramName) {
        FilterConfig config = this.getFilterConfig();
        return config != null?StringUtils.clean(config.getInitParameter(paramName)):null;
    }

    public FilterConfig getFilterConfig() {
        return this.filterConfig;
    }

    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    public String getFilterName() {
        FilterConfig config = this.getFilterConfig();
        return config != null?config.getFilterName():null;
    }

    public final void init(FilterConfig filterConfig) throws ServletException {
        this.setFilterConfig(filterConfig);
        this.doInitInternal();
    }

    public final void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            HttpServletRequest httpRequest = (HttpServletRequest)request;
            HttpServletResponse httpResponse = (HttpServletResponse)response;
            String alreadyFilteredAttributeName = this.getAlreadyFilteredAttributeName();
            boolean hasAlreadyFilteredAttribute = request.getAttribute(alreadyFilteredAttributeName) != null;
            if(!hasAlreadyFilteredAttribute && !this.shouldNotFilter(httpRequest)) {
                request.setAttribute(alreadyFilteredAttributeName, Boolean.TRUE);

                try {
                    this.doFilterInternal(httpRequest, httpResponse, filterChain);
                } finally {
                    request.removeAttribute(alreadyFilteredAttributeName);
                }
            } else {
                filterChain.doFilter(request, response);
            }

        } else {
            throw new ServletException("OncePerRequestFilter just supports HTTP requests");
        }
    }

    protected String getAlreadyFilteredAttributeName() {
        String name = this.getFilterName();
        if(name == null) {
            name = this.getClass().getName();
        }

        return name + ".FILTERED";
    }

    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return false;
    }

    protected abstract void doFilterInternal(HttpServletRequest var1, HttpServletResponse var2, FilterChain var3) throws ServletException, IOException;

    protected abstract void doInitInternal();
}
