package com.zhangry.demo.common.util;

import com.google.common.base.Throwables;
import java.util.List;

/**
 * Created by zhangry on 2017/3/17.
 */
public class ExceptionUtil {
    private ExceptionUtil() {
    }

    public static RuntimeException unchecked(Throwable throwable) {
        return throwable instanceof RuntimeException?(RuntimeException)throwable:new RuntimeException(throwable);
    }

    public static RuntimeException unchecked(String msg, Throwable throwable) {
        return throwable instanceof RuntimeException?(RuntimeException)throwable:new RuntimeException(msg, throwable);
    }

    public static String getStackTraceAsString(Throwable throwable) {
        return Throwables.getStackTraceAsString(throwable);
    }

    public static List<Throwable> getCausalChain(Throwable throwable) {
        return Throwables.getCausalChain(throwable);
    }

    public static Throwable getRootCause(Throwable throwable) {
        Throwable cause;
        while((cause = throwable.getCause()) != null) {
            throwable = cause;
        }

        return throwable;
    }
}

