package com.zhangry.demo.sso.client.internal;

import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhangry on 2017/3/28.
 */
public interface RequestHandler {
    void init(Map<String, String> var1);

    boolean handleRequest(HttpServletRequest var1, HttpServletResponse var2) throws ServletException, IOException;
}
