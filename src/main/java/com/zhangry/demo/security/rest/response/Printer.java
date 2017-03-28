package com.zhangry.demo.security.rest.response;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhangry on 2017/3/28.
 */
public class Printer {
    public Printer() {
    }

    public static void printMessage(HttpServletResponse response, String message) throws IOException {
        PrintWriter printWriter = response.getWriter();

        try {
            printWriter.print(message);
        } catch (Exception var7) {
            var7.printStackTrace();
        } finally {
            if(printWriter != null) {
                printWriter.flush();
                printWriter.close();
                printWriter = null;
            }

        }

    }
}
