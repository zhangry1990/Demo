package com.zhangry.demo.sso.client;

import com.thinvent.sso.client.internal.AntPathMatcher;
import com.thinvent.sso.client.internal.HttpServletRequestWrapper;
import com.thinvent.sso.client.internal.LoginHandler;
import com.thinvent.sso.client.internal.LogoutHandler;
import com.thinvent.sso.client.internal.OncePerRequestFilter;
import com.thinvent.sso.client.internal.RequestHandler;
import com.thinvent.sso.client.internal.StringUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.util.ReflectUtils;
import org.jasig.cas.client.validation.Assertion;

/**
 * Created by zhangry on 2017/3/28.
 */
public class SsoFilter extends OncePerRequestFilter implements SsoCallback, SsoConsts {
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final RequestHandler logoutHandler = new LogoutHandler();
    private final RequestHandler loginHandler = new LoginHandler(this);
    private List<String> ignorePaths = new ArrayList();
    private SsoCallback callback = null;
    protected final Logger Log = Logger.getLogger(this.getClass().getName());

    public SsoFilter() {
    }

    protected final void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(this.logoutHandler.handleRequest(request, response)) {
            if(this.loginHandler.handleRequest(request, response)) {
                filterChain.doFilter(wrapHttpServletRequest(request), response);
            }
        }
    }

    protected final void doInitInternal() {
        Map<String, String> initParamMap = toInitParamMap(this.getFilterConfig());
        String callbackClassName = (String)initParamMap.get("sso-callback-class");
        if(StringUtils.hasText(callbackClassName)) {
            try {
                Object object = ReflectUtils.newInstance(callbackClassName, new Object[0]);
                if(!SsoCallback.class.isAssignableFrom(object.getClass())) {
                    throw new IllegalArgumentException("SsoFilter 的 Init-Param 参数'sso-callback-class'必须实现 SsoCallback接口并且具有无参构造器!");
                }

                this.callback = (SsoCallback)object;
            } catch (Exception var5) {
                this.Log.log(Level.SEVERE, String.format("SsoFilter: 指定的Init-Param'%s'无效(%s)", new Object[]{"sso-callback-class", var5.getMessage()}));
            }
        }

        if(this.callback == null && this.getClass().equals(SsoFilter.class)) {
            this.Log.log(Level.SEVERE, "SsoFilter: 必须指定有效的'%s' 或者扩展 SsoFilter 覆盖 postLogin 方法!");
        }

        String ignorePathList = (String)initParamMap.get("ignore-path-list");
        if(StringUtils.hasText(ignorePathList)) {
            String[] paths = StringUtils.split(ignorePathList, ',');
            if(paths != null && paths.length > 0) {
                this.ignorePaths.clear();
                this.ignorePaths.addAll(Arrays.asList(paths));
            }
        }

        this.logoutHandler.init(initParamMap);
        this.loginHandler.init(initParamMap);
    }

    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String pathToCheck = request.getRequestURI().substring(request.getContextPath().length());
        Iterator i$ = this.ignorePaths.iterator();

        String ignorePath;
        do {
            if(!i$.hasNext()) {
                return false;
            }

            ignorePath = (String)i$.next();
        } while(!this.pathMatcher.match(ignorePath, pathToCheck));

        return true;
    }

    private static HttpServletRequest wrapHttpServletRequest(HttpServletRequest request) {
        if(!(request instanceof HttpServletRequestWrapper)) {
            Assertion assertion = (Assertion)request.getSession().getAttribute("__CONST_SSO_ASSERTION__");
            AttributePrincipal principal = assertion != null?assertion.getPrincipal():null;
            return new HttpServletRequestWrapper(request, new SsoPrincipal(principal));
        } else {
            return request;
        }
    }

    private static Map<String, String> toInitParamMap(FilterConfig config) {
        Map<String, String> initParams = new HashMap();
        Enumeration paramNames = config.getInitParameterNames();

        while(paramNames.hasMoreElements()) {
            String name = (String)paramNames.nextElement();
            String value = config.getInitParameter(name);
            if(StringUtils.hasText(value)) {
                initParams.put(name, value);
            }
        }

        return initParams;
    }

    public void destroy() {
    }

    public void postLogin(SsoPrincipal principal, HttpServletRequest request, HttpServletResponse response) {
        if(this.callback != null) {
            this.callback.postLogin(principal, request, response);
        }

    }
}