package com.moving.admin.exception;

import java.util.HashMap;
import java.util.Map;

import com.moving.admin.exception.WebException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@ControllerAdvice
@EnableWebMvc
public class ExceptionControllerAdvice {
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Map<String, Object> errorHandler(Exception ex) {
        Map<String, Object> map = new HashMap<String, Object>();
        String message = ex.getMessage();
//        ex.printStackTrace();
        message = message != null && message != "" ? message : "请求处理异常";
        if (message.indexOf("Required request body is missing") > -1) {
            message = "请传递所需的json数据,并指定Content-Type";
        }
        map.put("code", 400);
        map.put("message", message);
        map.put("data", null);
        return map;
    }

    @ResponseBody
    @ExceptionHandler(value = WebException.class)
    public Map<String, Object> myErrorHandler(WebException ex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", ex.getCode());
        map.put("message", ex.getMessage());
        map.put("data", ex.getData());
        return map;
    }
}
