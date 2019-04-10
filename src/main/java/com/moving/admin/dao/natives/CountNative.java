package com.moving.admin.dao.natives;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CountNative extends AbstractNative {

    // 获取人才常规跟踪的待办列表
    public Map<String, Object> talentRemindPendingList(Long userId, Integer type, Pageable pageable) {
        Map<String, Object> map = new HashMap<>();
        String select = "select r.id, r.type, r.status, r.remark, r.situation, r.cause, r.salary, r.meet_time as meetTime," +
                " (select count(1) from project_talent pt where pt.talent_id=t.id and pt.status<7) as projectCount," +
                " r.meet_address as meetAddress, r.create_time as createTime, t.name, t.phone, t.status as talentStatus, t.follow_user_id as followUserId, t.id as talentId";
        String countSelect = "select count(1)";
        String from = " from talent_remind r left join talent t on r.talent_id=t.id";
        String where = " where r.create_user_id=" + userId +" and r.finish=0";
        String sort = " order by r.id asc";
        if (type != null) {
            where = where + " and r.type=" + type;
        }
        Session session = entityManager.unwrap(Session.class);
        String sql = select + from + where + sort + limitStr(pageable);
        NativeQuery<Map<String, Object>> query = session.createNativeQuery(sql);
        query.addScalar("id", StandardBasicTypes.LONG);
        query.addScalar("talentId", StandardBasicTypes.LONG);
        query.addScalar("projectCount", StandardBasicTypes.LONG);
        query.addScalar("type", StandardBasicTypes.INTEGER);
        query.addScalar("phone", StandardBasicTypes.STRING);
        query.addScalar("status", StandardBasicTypes.INTEGER);
        query.addScalar("remark", StandardBasicTypes.STRING);
        query.addScalar("situation", StandardBasicTypes.STRING);
        query.addScalar("cause", StandardBasicTypes.STRING);
        query.addScalar("salary", StandardBasicTypes.STRING);
        query.addScalar("meetTime", StandardBasicTypes.TIMESTAMP);
        query.addScalar("meetAddress", StandardBasicTypes.STRING);
        query.addScalar("createTime", StandardBasicTypes.TIMESTAMP);
        query.addScalar("name", StandardBasicTypes.STRING);
        query.addScalar("talentStatus", StandardBasicTypes.INTEGER);
        query.addScalar("followUserId", StandardBasicTypes.LONG);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        map.put("content", query.getResultList());
        map.put("totalElements", getTotal(countSelect + from + where));
        return map;
    }

    // 获取客户常规跟踪的待办列表
    public Map<String, Object> customerRemindPendingList(Long userId, Integer type, Pageable pageable) {
        Map<String, Object> map = new HashMap<>();
        String select = "select r.id, r.type, r.status, r.remark, r.meet_time as meetTime, r.meet_address as meetAddress, r.meet_notice as meetNotice, " +
                "r.create_time as createTime, c.id as customerId, c.name, c.type as customerType, c.follow_user_id as followUserId";
        String countSelect = "select count(1)";
        String from  = " from customer_remind r left join customer c on r.customer_id=c.id";
        String where = " where r.create_user_id=" + userId + " and r.finish=0";
        String sort = " order by r.id asc";
        if (type != null) {
            where = where + " and r.type=" + type;
        }
        Session session = entityManager.unwrap(Session.class);
        String sql = select + from + where + sort + limitStr(pageable);
        NativeQuery<Map<String, Object>> query = session.createNativeQuery(sql);
        query.addScalar("id", StandardBasicTypes.LONG);
        query.addScalar("customerId", StandardBasicTypes.LONG);
        query.addScalar("followUserId", StandardBasicTypes.LONG);
        query.addScalar("type", StandardBasicTypes.INTEGER);
        query.addScalar("status", StandardBasicTypes.INTEGER);
        query.addScalar("customerType", StandardBasicTypes.INTEGER);
        query.addScalar("remark", StandardBasicTypes.STRING);
        query.addScalar("meetTime", StandardBasicTypes.DATE);
        query.addScalar("meetAddress", StandardBasicTypes.STRING);
        query.addScalar("meetNotice", StandardBasicTypes.STRING);
        query.addScalar("createTime", StandardBasicTypes.TIMESTAMP);
        query.addScalar("name", StandardBasicTypes.STRING);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        map.put("content", query.getResultList());
        map.put("totalElements", getTotal(countSelect + from + where));
        return map;
    }

    // 获取人才地图
    public List<Map<String, Object>> getTalentMapByUserId(Long userId) {
        String select = "select pt.id, pt.project_id as projectId, pt.talent_id as talentId, pt.status, pt.type, pt.create_time as createTime, pt.update_time as updateTime," +
                " p.name as projectName, t.name as talentName, t.status as talentStatus, t.follow_user_id as followuserId, c.id as customerId, c.name as customerName," +
                " d.id as departmentId, d.name as departmentName";
        String from = " from project_talent pt left join talent t on pt.talent_id=t.id " +
                "left join project p on pt.project_id=p.id left join customer c on p.customer_id=c.id left join department d on p.department_id=d.id ";
        String where = " where pt.talent_id in(select distinct tr.talent_id from talent_remind tr where tr.create_user_id=" + userId + ") " +
                "or pt.id in(select distinct project_talent_id from project_remind where create_user_id=" + userId +")";
        String sort = " order by p.customer_id, p.department_id, pt.status";
        String sql = select + from + where + sort;
        Session session = entityManager.unwrap(Session.class);
        NativeQuery<Map<String, Object>> query = session.createNativeQuery(sql);
        query.addScalar("id", StandardBasicTypes.LONG);
        query.addScalar("projectId", StandardBasicTypes.LONG);
        query.addScalar("talentId", StandardBasicTypes.LONG);
        query.addScalar("status", StandardBasicTypes.INTEGER);
        query.addScalar("type", StandardBasicTypes.INTEGER);
        query.addScalar("createTime", StandardBasicTypes.DATE);
        query.addScalar("updateTime", StandardBasicTypes.DATE);
        query.addScalar("projectName", StandardBasicTypes.STRING);
        query.addScalar("talentName", StandardBasicTypes.STRING);
        query.addScalar("talentStatus", StandardBasicTypes.INTEGER);
        query.addScalar("followUserId", StandardBasicTypes.LONG);
        query.addScalar("customerId", StandardBasicTypes.LONG);
        query.addScalar("customerName", StandardBasicTypes.STRING);
        query.addScalar("departmentId", StandardBasicTypes.LONG);
        query.addScalar("departmentName", StandardBasicTypes.STRING);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

    // 获取我联系过的且被收藏的人才
    public List<Map<String, Object>> getFolderTalentsByUserId(Long userId) {
        String sql = "select t.name from talent t where t.id in (select f.item_id where type=2) and (t.id in " +
                "(select distinct tr.talent_id from talent_remind tr where tr.create_user_id=" + userId + ") " +
                "or  t.id in (select distinct project_talent_id from project_remind where create_user_id=" + userId +")";
        Session session = entityManager.unwrap(Session.class);
        NativeQuery<Map<String, Object>> query = session.createNativeQuery(sql);
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
