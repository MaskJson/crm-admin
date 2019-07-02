package com.moving.admin.dao.natives;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
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

    // 获取下级成员ids
    public List<Long> getMemberIds(Long userId, Long roleId) {
        String sql = "";
        switch (Integer.parseInt(roleId.toString())) {
            case 2:
            case 6:
            case 7: sql = " select user_id from team where parent_id in(select id from team where level in(2,3,4) and user_id="+userId+")";break;
            case 3: sql = " select user_id from team where team_id in(select id from team where level=1 and user_id="+userId+")";break;
        }
        Session session = entityManager.unwrap(Session.class);
        NativeQuery<Map<String, Object>> query = session.createNativeQuery(sql);
        query.addScalar("user_id", StandardBasicTypes.LONG);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Long> ids = new ArrayList<>();
        List<Map<String, Object>> list = query.getResultList();
        list.forEach(item -> {
            ids.add(Long.parseLong(item.get("user_id").toString()));
        });
        return ids;
    }

    // 获取下级成员
    public List<Map<String, Object>> getMembers(Long userId, Long roleId, Integer flag) {
        String selectFrom = "select id, nick_name as nickName, role_id as roleId  from sys_user";
        String where = "";
        String levelFilter = flag == 1 ? "" : " level is null and ";
        String roleFilter = flag == 1 ? " (role_id=4 or role_id=5) " : " role_id <> 4 and role_id <> 5 ";
        switch (Integer.parseInt(roleId.toString())) {
            case 2:
            case 6:
            case 7: where = " where id="+userId+" or id in(select user_id from team where "+levelFilter+" parent_id in(select id from team where level in(2,3,4) and user_id="+userId+"))";break;
            case 3: where = " where id="+userId+" or  id in (select user_id from team where team_id in(select id from team where level=1 and user_id="+userId+"))";break;
            case 1: where = "";break;
        }
        Session session = entityManager.unwrap(Session.class);
        NativeQuery<Map<String, Object>> query = session.createNativeQuery(selectFrom + where);
        query.addScalar("id", StandardBasicTypes.LONG);
        query.addScalar("nickName", StandardBasicTypes.STRING);
        query.addScalar("roleId", StandardBasicTypes.LONG);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

    public void appendSort(Pageable pageable) {

    }
}
