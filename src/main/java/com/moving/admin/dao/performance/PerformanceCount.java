package com.moving.admin.dao.performance;

import com.moving.admin.dao.natives.AbstractNative;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

// 绩效页面的统计
@Service
public class PerformanceCount extends AbstractNative {

    public Map<String, Object> getPerformanceCount(Long userId, Long roleId, Long memberId) {
        Map<String, Object> map = new HashMap<>();
        map.put("map", count(userId, roleId, memberId, false));
        map.put("zhuanshu", count(userId, roleId, memberId, true));
        map.put("remind", remindCount(userId, roleId, memberId));
        return map;
    }
    // 人才地图总数 + 专属
    public BigInteger count(Long userId, Long roleId, Long memberId, Boolean b) {
        String sql = "select count(1) from talent" +
                getWhere(userId, roleId, memberId) +
                (b ? " and follow_user_id <> null" : "");
        Session session = entityManager.unwrap(Session.class);
        NativeQuery<Map<String, Object>> query = session.createNativeQuery(sql);
        return objectToBigInteger(query.getSingleResult());
    }

    // 跟踪预约未完成总数
    public BigInteger remindCount(Long userId, Long roleId, Long memberId) {
        String sql = "select count(1) from talent_remind" +
                " where (create_user_id=" + userId +
                getMemberWhere(userId, roleId, memberId, "create_user_id") + ") and finish=0";
        Session session = entityManager.unwrap(Session.class);
        NativeQuery<Map<String, Object>> query = session.createNativeQuery(sql);
        return objectToBigInteger(query.getSingleResult());
    }

    public String getWhere(Long userId, Long roleId, Long memberId) {
        String where = " where (id in(select distinct talent_id from talent_remind where create_user_id=" + userId + getMemberWhere(userId, roleId, memberId, "create_user_id") + ") " +
                "or id in(select distinct pt.talent_id from project_remind pr left join project_talent pt on pt.id=pr.project_talent_id where pr.create_user_id=" + userId +
                getMemberWhere(userId, roleId, memberId, "pr.create_user_id") +"))";
        return where;
    }

    public String getMemberWhere(Long userId, Long roleId, Long memberId, String str) {
        String memberStr = "";
        if (memberId != null) {
            memberStr = "";
        } else {
            switch (Integer.parseInt(roleId.toString())) {
                case 2:
                case 6: memberStr = " or " + str + " in(select user_id from team where parent_id in(select id from team where level in(2,3,4) and user_id="+userId+")))";break;
                case 3: memberStr = " or " + str + " in (select user_id from team where team_id in(select id from team where level=1 and user_id="+userId+")))";break;
                case 1: memberStr = " or 1=1";break;
                default: memberStr = "";break;
            }
        }
        return memberStr;
    }

    public void appendSort(Pageable pageable) {

    }

}
