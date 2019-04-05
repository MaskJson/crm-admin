package com.moving.admin.dao.natives;

import com.moving.admin.entity.sys.User;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

public class TeamNative extends AbstractNative<User> {

    private StringBuilder select = new StringBuilder("select u.id as id, u.nick_name as nickName");
    private StringBuilder count = new StringBuilder("select count(1)");
    private StringBuilder from = new StringBuilder(" from sys_user u");
    private StringBuilder where = new StringBuilder(" where u.role_id=3");
    private StringBuilder sort = new StringBuilder(" order by ");

    public List<Map<String, Object>> getTeamList(String name) {
        if (!StringUtils.isEmpty(name)) {
            where.append(" and u.nick_name = '" + name +"'");
        }
        String sql = select.append(from).append(where).append(sort).toString();
        Session session = entityManager.unwrap(Session.class);
        NativeQuery<Map<String, Object>> query = session.createNativeQuery(sql);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

    // 获取团队管理页面需要的用户列表
    public List<Map<String, Object>> getTeamManagerUsers() {
        String sql = "select id, nick_name as nickName, role_id from sys_user where role_id not in(1,3)";
        Session session = entityManager.unwrap(Session.class);
        NativeQuery<Map<String, Object>> query = session.createNativeQuery(sql);
        query.addScalar("id", StandardBasicTypes.LONG);
        query.addScalar("nickName", StandardBasicTypes.STRING);
        query.addScalar("roleId", StandardBasicTypes.INTEGER);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

    public void appendSort(Pageable pageable) {
        super.simpleAppendSort(pageable, sort);
    }

}
