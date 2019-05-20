package com.moving.admin.dao.performance;

import com.moving.admin.dao.natives.AbstractNative;
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
public class PerformanceReport extends AbstractNative {

    private final String select = "select r.id, r.content, r.create_user_id as createUserId, r.create_time as createTime, u.nick_name as createUser, u.role_id as roleId";
    private final String from  = " from report r left join sys_user u on r.create_user_id=u.id";
    private final String sort = " order by r.create_time";
    private final String where = " where r.create_user_id=";

    //报告 日、周、月绩效
    public List<Map<String, Object>> getPerformance(Long userId, Integer flag, String time) {
        String whereStr = " and r.type=" + flag;
        if (StringUtils.isEmpty(time)) {
            time = "now()";
        } else {
            time = "'"+time+"'";
        }
        switch (flag) {
            case 1: whereStr = " and to_days(r.create_time) = to_days("+time+")";break;
            case 2: whereStr = " and YEARWEEK(date_format(r.create_time, '%Y-%m-%d')) = YEARWEEK("+time+", '%Y-%m-%d')";break;
            case 3: whereStr = " and DATE_FORMAT(r.create_time, '%Y%m') = "+time;break;
        }
        return getPerformanceReport(where + userId + whereStr);
    }
    //报告日、 周、月报表
    public List<Map<String, Object>> getPerformanceReport(Long userId, Long roleId, Integer flag, String time) {
        String where = "";
        if (StringUtils.isEmpty(time)) {
            time = "now()";
        } else {
            time = "'"+time+"'";
        }
        switch (Integer.parseInt(roleId.toString())) {
            case 2:
            case 6:
            case 7: where = " where r.create_user_id in(select user_id from team where parent_id in(select id from team where level in(2,3,4) and user_id="+userId+"))";break;
            case 3: where = " where r.create_user_id in (select user_id from team where team_id in(select id from team where level=1 and user_id="+userId+"))";break;
            case 1: where = " where r.create_user_id in (select user_id from team where level=1)";break;
        }
        switch (flag) {
            case 1:where = where + " and to_days(r.create_time) = to_days("+time+")";break;
            case 2:where = where + " and YEARWEEK(date_format(r.create_time, '%Y-%m-%d')) = YEARWEEK("+time+", '%Y-%m-%d')";break;
            case 3:where = where + " and DATE_FORMAT(r.create_time, '%Y%m') = "+time;break;
        }
        return getPerformanceReport(where + " and r.type=" + flag);
    }

    // 获取下级成员
    public List<Map<String, Object>> getMembers(Long userId, Long roleId) {
        String selectFrom = "select id as createUserId, nick_name as nickName, role_id as roleId as name from sys_user";
        String where = "";
        switch (Integer.parseInt(roleId.toString())) {
            case 2:
            case 6:
            case 7: where = " where id in(select user_id from team where parent_id in(select id from team where level in(2,3,4) and user_id="+userId+"))";break;
            case 3: where = " where id in (select user_id from team where team_id in(select id from team where level=1 and user_id="+userId+"))";break;
            case 1: where = " where id in (select user_id from team where level=1)";break;
        }
        Session session = entityManager.unwrap(Session.class);
        NativeQuery<Map<String, Object>> query = session.createNativeQuery(selectFrom + where);
        query.addScalar("createUserId", StandardBasicTypes.LONG);
        query.addScalar("nickName", StandardBasicTypes.STRING);
        query.addScalar("roleId", StandardBasicTypes.LONG);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

    public List<Map<String, Object>> getPerformanceReport(String where) {
        Session session = entityManager.unwrap(Session.class);
        NativeQuery<Map<String, Object>> query = session.createNativeQuery(select + from + where + sort);
        query.addScalar("id", StandardBasicTypes.LONG);
        query.addScalar("content", StandardBasicTypes.STRING);
        query.addScalar("createUserId", StandardBasicTypes.LONG);
        query.addScalar("createTime", StandardBasicTypes.TIMESTAMP);
        query.addScalar("createUser", StandardBasicTypes.STRING);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.getResultList();
        return list;
    }

    public void appendSort(Pageable pageable) {}

}
