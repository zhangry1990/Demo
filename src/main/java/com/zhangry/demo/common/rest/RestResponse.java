package com.zhangry.demo.common.rest;

import java.io.Serializable;

/**
 * Created by zhangry on 2017/3/17.
 */
public class RestResponse implements Serializable {
    private static final String OK = "ok";
    private static final String ERROR = "error";
    private boolean success;
    private String message;
    private Object data;

    private RestResponse() {
    }

    public static RestResponse success() {
        RestResponse response = new RestResponse();
        response.success = true;
        response.message = "ok";
        return response;
    }

    public static RestResponse success(Object data) {
        RestResponse response = new RestResponse();
        response.success = true;
        response.message = "ok";
        response.data = data;
        return response;
    }

    public static RestResponse failure() {
        RestResponse response = new RestResponse();
        response.success = false;
        response.message = "error";
        return response;
    }

    public static RestResponse failure(String message) {
        RestResponse response = new RestResponse();
        response.success = false;
        response.message = message;
        return response;
    }

    public Object getData() {
        return this.data;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public String getMessage() {
        return this.message;
    }
}

