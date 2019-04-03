package com.moving.admin.dao.natives;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class BaseNative {

    @PersistenceContext
    protected EntityManager entityManager;

}
