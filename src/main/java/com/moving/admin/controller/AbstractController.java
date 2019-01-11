package com.moving.admin.controller;

import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractController {

    @PersistenceContext
    private EntityManager entityManager;

    protected Object emptyResp() {
        return new HashMap<String, String>();
    }
}
