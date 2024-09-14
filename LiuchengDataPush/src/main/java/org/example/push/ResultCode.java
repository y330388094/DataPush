package org.example.push;

public enum ResultCode {

    SUCCESS(200, "成功"),
    PARA_ERROR(400, "参数错误"),
    UNAUTHORIZED(401, "需要授权验证或token过期"),
    NOT_FOUND(403, "禁止访问"),
    INTERNAL_SERVER_ERROR(500, "服务器内部错误");

    private int code;
    private String message;

    public Integer code(){
        return this.code;
    }
    public String message(){
        return this.message;
    }
    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

 }

