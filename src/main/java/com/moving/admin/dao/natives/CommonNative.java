package com.moving.admin.dao.natives;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Service
public class CommonNative extends AbstractNative {

    // 根据name获取列表 id + name
    public List<Map<String, Object>> getListByTableName(String tableName, String name) {
        String nameKey = tableName.equals("sys_user") ? "g.nick_name as name" : tableName.equals("team") ? "u.nick_name as name" : "g.name as name";
        String select = "select g.id as id, " + nameKey ;
        String from = " from " + tableName + " g";
        String where = " where 1=1";
        String orderKey = tableName.equals("sys_user") ? "g.nick_name" : tableName.equals("team") ? "u.nick_name" : "g.name";
        String sort = " order by " + orderKey + " asc";
        if (!StringUtils.isEmpty(name) && !tableName.equals("sys_user") && !tableName.equals("")) {
            where = where + " and g.name like '%" + name + "%'";
        }
        if (tableName.equals("sys_user")) {
            where = where + " and g.role_id=8";
        }
        if (tableName.equals("team")) {
            where = where + " and g.level=1";
            from = from + " left join sys_user u on g.user_id=u.id";
        }
        Session session = entityManager.unwrap(Session.class);
        NativeQuery<Map<String, Object>> query = session.createNativeQuery(select + from + where + sort);
        query.addScalar("id", StandardBasicTypes.LONG);
        query.addScalar("name", StandardBasicTypes.STRING);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

    public void appendSort(Pageable pageable) {

    }
}
