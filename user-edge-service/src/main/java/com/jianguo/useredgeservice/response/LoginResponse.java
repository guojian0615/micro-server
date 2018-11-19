package com.jianguo.useredgeservice.response;

import com.jianguo.useredgeservice.eum.ReturnCodeEum;

public class LoginResponse extends ResponseBase {
    private String token;

    public LoginResponse(String token, ReturnCodeEum returnCodeEum) {
        super(returnCodeEum);
        this.token = token;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
