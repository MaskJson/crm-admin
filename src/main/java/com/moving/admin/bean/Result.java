package com.moving.admin.bean;


public class Result<T> {

    /**
     * 错误码.
     */
    private Integer code;

    /**
     * 提示信息.
     */
    private String message;

    /**
     * 具体的内容.
     */
    private T data;

    public Result() {

    }

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}