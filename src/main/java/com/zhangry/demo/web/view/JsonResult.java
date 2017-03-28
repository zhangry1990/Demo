package com.zhangry.demo.web.view;

/**
 * Created by zhangry on 2017/3/28.
 */
public class JsonResult {
    private boolean result = false;
    private Object data = null;
    private String message = null;

    public JsonResult() {
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public boolean getResult() {
        return this.result;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return this.data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}

