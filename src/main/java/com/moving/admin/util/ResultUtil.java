package com.moving.admin.util;


import com.moving.admin.bean.Result;

public class ResultUtil<T> {
    private static int code = 200;
    private static String message = "成功的返回";

    public static <T> Result<T> success(T obj) {
        return new Result<>(code, message, obj);
    }

    public static <T> Result<T> error(String message) {
        return new Result<>(400, message, null);
    }

    public static <T> Result<T> WarningHandler(T obj, String message, int code) {
        return new Result<>(code, message, obj);
    }
}
