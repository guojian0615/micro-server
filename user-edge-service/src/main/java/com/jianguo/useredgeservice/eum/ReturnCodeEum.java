package com.jianguo.useredgeservice.eum;

public enum ReturnCodeEum {
    SUCCESS("0000", "success"),
    USERNAME_OR_PWD_ISINVALID("1001", "用户名或密码错误"),
    ERROR("9999", "error");
    private String code;
    private String message;

    ReturnCodeEum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
