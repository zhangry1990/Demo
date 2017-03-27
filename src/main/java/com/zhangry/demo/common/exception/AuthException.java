package com.zhangry.demo.common.exception;

/**
 * Created by zhangry on 2017/3/17.
 */
public class AuthException extends RuntimeException {
    private static final long serialVersionUID = -8171245571221202343L;

    public AuthException(String message) {
        super(message);
    }

    public AuthException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getCasuseMessage() {
        return super.getCause() != null?super.getCause().getMessage():super.getMessage();
    }

    public String getMessage() {
        return super.getCause() != null?String.format("%s-%s", new Object[]{super.getMessage(), super.getCause().getMessage()}):super.getMessage();
    }
}
