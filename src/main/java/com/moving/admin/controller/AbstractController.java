package com.moving.admin.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractController {
    @Autowired
    public HttpServletRequest request;

    @Autowired
    public HttpServletResponse response;

    protected Object emptyResp() {
        return new HashMap<String, String>();
    }
}
