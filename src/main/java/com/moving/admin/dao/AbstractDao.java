package com.moving.admin.dao;
import java.lang.reflect.ParameterizedType;

public abstract class AbstractDao<T> {

    /**
     * 当前类（表名）
     */
    protected Class<T> clazz;

    /**
     * 表名
     */
    protected String tableName;

    public AbstractDao() {
        clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        tableName = clazz.getSimpleName();
    }

}
