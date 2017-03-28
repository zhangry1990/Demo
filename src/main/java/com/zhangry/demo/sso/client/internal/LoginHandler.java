package com.zhangry.demo.sso.client.internal;

import com.thinvent.sso.client.SsoCallback;
import com.thinvent.sso.client.SsoConsts;
import com.thinvent.sso.client.SsoPrincipal;
import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.jasig.cas.client.validation.TicketValidationException;
import org.jasig.cas.client.validation.TicketValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhangry on 2017/3/28.
 */
public class LoginHandler implements RequestHandler, SsoConsts {
    private static Logger logger = LoggerFactory.getLogger(LoginHandler.class);
    private final SsoCallback callback;
    private String koalLoginURL;
    private String ssoServerURL;
    private String loginFailedPage;
    private String loginSuccessPage;
    private TicketValidator ticketValidator;

    public LoginHandler(SsoCallback callback) {
        this.callback = callback;
    }

    public boolean handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String originURL;
        String redirectURL;
        if(this.isTicketRequest(request)) {
            String ticketId = request.getParameter("ticket");

            try {
                originURL = constructServiceUrl(request);
                Assertion assertion = this.ticketValidator.validate(ticketId, constructServiceUrl(request));
                request.getSession().setAttribute("__CONST_SSO_ASSERTION__", assertion);
                this.callback.postLogin(new SsoPrincipal(assertion.getPrincipal()), request, response);
                redirectURL = (String)request.getSession().getAttribute("__CONST_ORIGINAL_URL__");
                if(StringUtils.hasText(redirectURL)) {
                    response.sendRedirect(redirectURL);
                    request.getSession().removeAttribute("__CONST_ORIGINAL_URL__");
                } else {
                    response.sendRedirect(request.getContextPath() + this.loginSuccessPage);
                }
            } catch (TicketValidationException var7) {
                if(StringUtils.hasText(this.loginFailedPage)) {
                    request.setAttribute("LOGIN_EXCEPTION", var7);
                    request.getRequestDispatcher(this.loginFailedPage).forward(request, response);
                } else {
                    response.sendError(403, var7.getMessage());
                }
            } catch (Exception var8) {
                request.getRequestDispatcher("/common/error/sso-error.jsp").forward(request, response);
            }

            return false;
        } else {
            Assertion assertion = (Assertion)request.getSession().getAttribute("__CONST_SSO_ASSERTION__");
            if(assertion != null && assertion.isValid()) {
                return true;
            } else {
                originURL = request.getRequestURL().toString();
                if(StringUtils.hasText(request.getQueryString())) {
                    originURL = originURL + '?' + request.getQueryString();
                }

                request.getSession().setAttribute("__CONST_ORIGINAL_URL__", originURL);
                String serviceURL = constructServiceUrl(request);
                redirectURL = this.koalLoginURL != null?this.koalLoginURL:this.ssoServerURL + "/login";
                response.sendRedirect(response.encodeRedirectURL(redirectURL + "?service=" + serviceURL));
                return false;
            }
        }
    }

    private boolean isSsoRequest(HttpServletRequest request) {
        return request.getRequestURI().endsWith("/sso-service");
    }

    private boolean isTicketRequest(HttpServletRequest request) {
        return this.isSsoRequest(request) && CommonUtils.isNotBlank(CommonUtils.safeGetParameter(request, "ticket"));
    }

    private static String constructServiceUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String host = request.getServerName();
        StringBuilder builder = new StringBuilder();
        builder.append(scheme).append("://");
        builder.append(host);
        if(request.getServerPort() != 80 && request.getServerPort() != 443) {
            builder.append(':');
            builder.append(request.getServerPort());
        }

        builder.append(request.getContextPath());
        builder.append("/sso-service");
        return builder.toString();
    }

    public void init(Map<String, String> initParams) {
        this.ssoServerURL = (String)initParams.get("sso-server-url");
        this.koalLoginURL = (String)initParams.get("koal-login-url");
        this.loginSuccessPage = (String)initParams.get("login-success-page");
        this.loginFailedPage = (String)initParams.get("login-failed-page");
        CommonUtils.assertNotNull(this.ssoServerURL, "必须为 SsoFilter 指定 Init-Param 参数: sso-server-url");
        this.ticketValidator = new Cas20ServiceTicketValidator(this.ssoServerURL);
        ((Cas20ServiceTicketValidator)this.ticketValidator).setEncoding("UTF-8");
    }
}
