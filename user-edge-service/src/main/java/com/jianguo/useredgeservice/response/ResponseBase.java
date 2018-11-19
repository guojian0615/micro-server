package com.jianguo.useredgeservice.response;

import com.jianguo.useredgeservice.eum.ReturnCodeEum;

import java.io.Serializable;

public class ResponseBase {
    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResponseBase(ReturnCodeEum returnCodeEum) {
        this.code = returnCodeEum.getCode();
        this.message = returnCodeEum.getMessage();
    }
}
