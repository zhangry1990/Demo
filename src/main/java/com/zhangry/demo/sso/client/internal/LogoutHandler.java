package com.zhangry.demo.sso.client.internal;

import com.thinvent.sso.client.SsoConsts;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jasig.cas.client.session.SingleSignOutHandler;

/**
 * Created by zhangry on 2017/3/28.
 */
public class LogoutHandler implements RequestHandler, SsoConsts {
    private final SingleSignOutHandler casHandler = new SingleSignOutHandler();
    private final AtomicBoolean casHhandlerInitialized = new AtomicBoolean(false);

    public LogoutHandler() {
    }

    public boolean handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        return this.casHandler.process(request, response);
    }

    public void init(Map<String, String> initParams) {
        String ssoServerURL = (String)initParams.get("sso-server-url");
        if(StringUtils.hasText(ssoServerURL)) {
            this.casHandler.setCasServerUrlPrefix(ssoServerURL);
        }

        if(!this.casHhandlerInitialized.getAndSet(true)) {
            this.casHandler.init();
        }

    }
}

