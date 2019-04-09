package com.moving.admin.dao.natives;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.data.domain.Pageable;

import javax.persistence.Query;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountNative extends AbstractNative {

    // 获取人才常规跟踪的待办列表
    public Map<String, Object> talentRemindPendingList(Long userId, Integer type, Pageable pageable) {
        Map<String, Object> map = new HashMap<>();
        String select = "select r.id, r.talent_id as talentId, r.status, r.remark, r.situation, r.cause, r.salary, r.meet_time as meetTime," +
                " meet_address as meetAddress, t.name, t.status as talentStatus, t.follow_user_id";
        String countSelect = "select count(1)";
        String from = " from talent_remind r left join talent t on r.talent_id=t.id";
        String where = " where r.create_user_id=" + userId;
        String sort = " order by r.id asc";
        if (type != null) {
            where = where + " and r.type=" + type;
        }
        Session session = entityManager.unwrap(Session.class);
        String sql = select + from + where + sort + limitStr(pageable);
        NativeQuery<Map<String, Object>> query = session.createNativeQuery(sql);
        query.addScalar("id", StandardBasicTypes.LONG);
        query.addScalar("talentId", StandardBasicTypes.LONG);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        map.put("content", query.getResultList());
        map.put("totalElements", getTotal(countSelect + from + where));
        return map;
    }

    // 获取客户常规跟踪的待办列表
    public List<Map<String, Object>> customerRemindPendingList(Long userId, Integer type, Pageable pageable) {
        return null;
    }

    // 获取分页sql
    public String limitStr(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int start = pageable.getPageNumber()*pageSize;
        return " limit " + start + "," + pageSize;
    }

    // 获取总数
    public BigInteger getTotal(String sql) {
        Query query = entityManager.createNativeQuery(sql);
        return objectToBigInteger(query.getSingleResult());
    }

    public void appendSort(Pageable pageable) {}
}
