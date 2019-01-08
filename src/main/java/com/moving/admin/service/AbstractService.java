package com.moving.admin.service;

import java.sql.Timestamp;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.moving.admin.bean.TokenInformation;
import com.moving.admin.exception.WebException;
import com.moving.admin.util.JwtUtil;


public class AbstractService {
    @Resource
    private HttpServletRequest request;
    @Resource
    private JwtUtil jwtUtil;

    public Long getCurrentUserID() {
        String jwt = request.getHeader("token");
        if (jwt == null || jwt.equals("")) {
            throw new WebException(401, "未登录", null);
        }
        Map<String, Object> map = jwtUtil.decode(jwt, TokenInformation.class);
        TokenInformation token = (TokenInformation) map.get("token");
        boolean overtime = (boolean) map.get("overtime");
        if (overtime) {
            throw new WebException(401, "登录超时", null);
        }
        return token.getId();
    }

    public Timestamp now() {
        return new Timestamp(System.currentTimeMillis());
    }
}
