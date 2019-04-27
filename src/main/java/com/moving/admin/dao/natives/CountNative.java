package com.moving.admin.dao.natives;

import com.moving.admin.dao.project.ProjectTalentDao;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CountNative extends AbstractNative {

    @Autowired
    private ProjectTalentDao projectTalentDao;

    // 获取人才常规跟踪的待办列表
    public Map<String, Object> talentRemindPendingList(Long userId, Integer type, Pageable pageable) {
        Map<String, Object> map = new HashMap<>();
        String select = "select r.id, r.type, r.status, r.remark, r.situation, r.cause, r.salary, r.meet_time as meetTime," +
                " (select count(1) from project_talent pt where pt.talent_id=t.id and pt.status<7) as projectCount," +
                " r.meet_address as meetAddress, r.create_time as createTime, t.name, t.phone, t.status as talentStatus, t.follow_user_id as followUserId, t.id as talentId";
        String countSelect = "select count(1)";
        String from = " from talent_remind r left join talent t on r.talent_id=t.id";
        String where = " where r.create_user_id=" + userId +" and r.finish=0 and r.status<>10 and now()>r.next_remind_time";
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
        String where = " where r.create_user_id=" + userId + " and r.finish=0 and now()>r.next_remind_time";
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
        query.addScalar("meetTime", StandardBasicTypes.TIMESTAMP);
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
        String select = "select id, name as talentName, status, follow_user_id as followUserId, type as talentType";
        String from = " from talent";
        String where = " where id in(select distinct talent_id from talent_remind where create_user_id=" + userId + ") " +
                "or id in(select distinct pt.talent_id from project_remind pr left join project_talent pt on pt.id=pr.project_talent_id where pr.create_user_id=" + userId +")";
        String sql = select + from + where;
        Session session = entityManager.unwrap(Session.class);
        NativeQuery<Map<String, Object>> query = session.createNativeQuery(sql);
        query.addScalar("id", StandardBasicTypes.LONG);
        query.addScalar("talentName", StandardBasicTypes.STRING);
        query.addScalar("status", StandardBasicTypes.INTEGER);
        query.addScalar("talentType", StandardBasicTypes.INTEGER);
        query.addScalar("followUserId", StandardBasicTypes.LONG);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> talentList = query.getResultList();
        talentList.forEach(map -> {
            Long id = Long.parseLong(map.get("id").toString());
            map.put("info", getWorkInfo(id));
            map.put("remind", getRemindInfo(id));
            map.put("progress", projectTalentDao.getProjectLengthByTalentId(id));
            map.put("projects", projectTalentDao.findProjectIdsOfTalent(id));
            map.put("offerCount", projectTalentDao.getProjectOfferLength(id));
        });
        return talentList;
    }

    // 获取我联系过的且被收藏的人才
    public List<Map<String, Object>> getFolderTalentsByUserId(Long userId) {
        String sql = "select t.id, t.name as talentName, t.status, t.follow_user_id as followUserId, t.type as talentType  from talent t where t.id in (select item_id from folder_item where type=2) and (t.id in " +
                "(select distinct tr.talent_id from talent_remind tr where tr.create_user_id=" + userId + ") " +
                "or  t.id in (select distinct project_talent_id from project_remind where create_user_id=" + userId +"))";
        Session session = entityManager.unwrap(Session.class);
        NativeQuery<Map<String, Object>> query = session.createNativeQuery(sql);
        query.addScalar("id", StandardBasicTypes.LONG);
        query.addScalar("city", StandardBasicTypes.STRING);
        query.addScalar("phone", StandardBasicTypes.STRING);
        query.addScalar("talentName", StandardBasicTypes.STRING);
        query.addScalar("status", StandardBasicTypes.INTEGER);
        query.addScalar("talentType", StandardBasicTypes.INTEGER);
        query.addScalar("followUserId", StandardBasicTypes.LONG);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> talentList = query.getResultList();
        talentList.forEach(map -> {
            Long id = Long.parseLong(map.get("id").toString());
            map.put("info", getWorkInfo(id));
            map.put("remind", getRemindInfo(id));
            map.put("progress", projectTalentDao.getProjectLengthByTalentId(id));
            map.put("projects", projectTalentDao.findProjectIdsOfTalent(id));
            map.put("offerCount", projectTalentDao.getProjectOfferLength(id));
        });
        return talentList;
    }

    // 获取人才地图的最后一次跟踪
    public Map<String, Object> getRemindInfo(Long talentId) {
        String sql = "select r.type, r.remark, r.situation, r.cause, r.salary, r.meet_time as meetTime, r.meet_address as meetAddress, u.nick_name as createUser, r.create_time as createTime" +
                " from talent_remind r left join sys_user u on r.create_user_id=u.id where r.talent_id=" + talentId + " order by r.create_time desc";
        Session session = entityManager.unwrap(Session.class);
        NativeQuery<Map<String, Object>> query = session.createNativeQuery(sql);
        query.addScalar("type", StandardBasicTypes.INTEGER);
        query.addScalar("remark", StandardBasicTypes.STRING);
        query.addScalar("situation", StandardBasicTypes.STRING);
        query.addScalar("cause", StandardBasicTypes.STRING);
        query.addScalar("salary", StandardBasicTypes.STRING);
        query.addScalar("meetTime", StandardBasicTypes.TIMESTAMP);
        query.addScalar("meetAddress", StandardBasicTypes.STRING);
        query.addScalar("createUser", StandardBasicTypes.STRING);
        query.addScalar("createTime", StandardBasicTypes.TIMESTAMP);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.getResultList();
        if (list.size() > 0) {
            return list.get(0);
        }
        return new HashMap<>();
    }

    // 获取人才地图的最后一份工作信息
    public Map<String, Object> getWorkInfo(Long talentId) {
        System.err.println(talentId);
        String sql = "select e.customer_id as customerId, e.department_id as departmentId, e.position, c.name as customerName, d.name as departmentName" +
                " from experience e left join customer c on e.customer_id=c.id left join department d on e.department_id=d.id" +
                " where e.status=1 and e.talent_id=" + talentId;
        Session session = entityManager.unwrap(Session.class);
        NativeQuery<Map<String, Object>> query = session.createNativeQuery(sql);
        query.addScalar("customerId", StandardBasicTypes.LONG);
        query.addScalar("departmentId", StandardBasicTypes.LONG);
        query.addScalar("position", StandardBasicTypes.STRING);
        query.addScalar("customerName", StandardBasicTypes.STRING);
        query.addScalar("departmentName", StandardBasicTypes.STRING);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.getResultList();
        if (list.size() > 0) {
            return list.get(0);
        }
        return new HashMap<>();
    }

    // 获取我联系过的经过各个状态的项目进展人才
    public List<Map<String, Object>> getProjectStatusTalents(Long userId) {
        String select = "select pr.status as remindStatus, pt.id, pt.project_id as projectId, pt.talent_id as talentId, pt.status, pt.type, pt.create_time as createTime, pt.update_time as updateTime," +
                " p.name as projectName, t.name as talentName, t.status as talentStatus, t.type as talentType, t.follow_user_id as followuserId, c.id as customerId, c.name as customerName," +
                " d.id as departmentId, d.name as departmentName";
        String from = " from project_remind pr left join project_talent pt on pr.project_talent_id=pt.id " +
                " left join project p on pt.project_id=p.id left join talent t on pt.talent_id=t.id" +
                " left join customer c on p.customer_id=c.id left join department d on p.department_id=d.id";
        String where = " where pr.create_user_id=" + userId + " and pr.status in (1,3,4,7) and pt.talent_id in(select distinct tr.talent_id from talent_remind tr where tr.create_user_id=" + userId + ")";
        String sort = " group by pr.project_talent_id, pr.status order by p.customer_id, p.department_id, pt.status";
        Session session = entityManager.unwrap(Session.class);
        NativeQuery<Map<String, Object>> query = session.createNativeQuery(select + from + where + sort);
        query.addScalar("remindStatus", StandardBasicTypes.INTEGER);
        query.addScalar("id", StandardBasicTypes.LONG);
        query.addScalar("projectId", StandardBasicTypes.LONG);
        query.addScalar("talentId", StandardBasicTypes.LONG);
        query.addScalar("status", StandardBasicTypes.INTEGER);
        query.addScalar("type", StandardBasicTypes.INTEGER);
        query.addScalar("talentType", StandardBasicTypes.INTEGER);
        query.addScalar("createTime", StandardBasicTypes.TIMESTAMP);
        query.addScalar("updateTime", StandardBasicTypes.TIMESTAMP);
        query.addScalar("projectName", StandardBasicTypes.STRING);
        query.addScalar("talentName", StandardBasicTypes.STRING);
        query.addScalar("talentStatus", StandardBasicTypes.INTEGER);
        query.addScalar("followUserId", StandardBasicTypes.LONG);
        query.addScalar("customerId", StandardBasicTypes.LONG);
        query.addScalar("customerName", StandardBasicTypes.STRING);
        query.addScalar("departmentId", StandardBasicTypes.LONG);
        query.addScalar("departmentName", StandardBasicTypes.STRING);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> talentList = query.getResultList();
        talentList.forEach(map -> {
            Long id = Long.parseLong(map.get("talentId").toString());
            map.put("info", getWorkInfo(id));
            map.put("remind", getRemindInfo(id));
            map.put("progress", projectTalentDao.getProjectLengthByTalentId(id));
            map.put("projects", projectTalentDao.findProjectIdsOfTalent(id));
            map.put("offerCount", projectTalentDao.getProjectOfferLength(id));
        });
        return talentList;
    }

    // 获取未处理的诊断提醒记录
    public Map<String, Object> getReportPending(Long userId, Pageable pageable) {
        Map<String, Object> map = new HashMap<>();
        String inStr = "(select team_id from team where user_id=" + userId + ")";
        String select = "select r.id, r.remark, u.nick_name as createUser, r.create_time as createTime, p.name as projectName, p.id as projectId";
        String countSelect = "select count(1)";
        String from = " from project_report r left join project p on r.project_id=p.id left join sys_user u on u.id=r.create_user_id";
        String where = " where r.status=1 and r.type=1 and p.team_id is not null and p.team_id in " + inStr;
        String sort = " order by r.create_time asc";
        Session session = entityManager.unwrap(Session.class);
        NativeQuery<Map<String, Object>> query = session.createNativeQuery(select + from + where + sort + limitStr(pageable));
        query.addScalar("id", StandardBasicTypes.LONG);
        query.addScalar("projectId", StandardBasicTypes.LONG);
        query.addScalar("createUser", StandardBasicTypes.STRING);
        query.addScalar("createTime", StandardBasicTypes.TIMESTAMP);
        query.addScalar("projectName", StandardBasicTypes.STRING);
        query.addScalar("remark", StandardBasicTypes.STRING);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        map.put("content", query.getResultList());
        map.put("totalElements", getTotal(countSelect + from + where));
        return map;
    }

    // 项目总监获取项目诊断报告
    public Map<String, Object> getReports(Long userId, Pageable pageable) {
        Map<String, Object> map = new HashMap<>();
        String select = "select r.id, r.create_time as createTime, u.nick_name as createUser, sr.role_name as roleName, p.name as projectName, p.id as projectId, " +
                "r.all_count as allCount, r.recommend_count as recommendCount, r.interview_count as interviewCount, r.offer_count as offerCount, r.working_count as workingCount, " +
                "r.quality_count as qualityCount, r.quality_pass_count as qualityPassCount";
        String countSelect = "select count(1)";
        String from = " from project_report r left join project p on r.project_id=p.id left join sys_user u on u.id=r.create_user_id left join sys_role sr on sr.id=u.role_id";
        String where = " where p.create_user_id=" + userId +" or p.open_type=1";
        String sort = " order by p.id asc, r.create_time desc";
        Session session = entityManager.unwrap(Session.class);
        NativeQuery<Map<String, Object>> query = session.createNativeQuery(select + from + where + sort + limitStr(pageable));
        query.addScalar("id", StandardBasicTypes.LONG);
        query.addScalar("allCount", StandardBasicTypes.BIG_INTEGER);
        query.addScalar("recommendCount", StandardBasicTypes.BIG_INTEGER);
        query.addScalar("interviewCount", StandardBasicTypes.BIG_INTEGER);
        query.addScalar("offerCount", StandardBasicTypes.BIG_INTEGER);
        query.addScalar("workingCount", StandardBasicTypes.BIG_INTEGER);
        query.addScalar("qualityCount", StandardBasicTypes.BIG_INTEGER);
        query.addScalar("qualityPassCount", StandardBasicTypes.BIG_INTEGER);
        query.addScalar("projectName", StandardBasicTypes.STRING);
        query.addScalar("roleName", StandardBasicTypes.STRING);
        query.addScalar("createUser", StandardBasicTypes.STRING);
        query.addScalar("createTime", StandardBasicTypes.TIMESTAMP);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        map.put("content", query.getResultList());
        map.put("totalElements", getTotal(countSelect + from + where));
        return map;
    }

    // 项目总监获取未处理的人才推荐审核
    public Map<String, Object> getRecommendListByUserId(Long userId, Pageable pageable) {
        Map<String, Object> map = new HashMap<>();
        String select = "select pr.id, pt.id as projectTalentId, p.id as projectId, t.id as talentId, t.follow_user_id as followUserId," +
                " t.status as talentStatus, t.name as talentName, p.name as projectName," +
                " u.nick_name as createUser, pr.create_time as createTime";
        String countSelect = "select count(1)";
        String from = " from project_remind pr left join project_talent pt on pr.project_talent_id=pt.id" +
                " left join project p on p.id=pt.project_id left join talent t on pt.talent_id=t.id" +
                " left join sys_user u on u.id=pr.create_user_id";
        String where = " where  pr.type=100 and (p.create_user_id=" + userId + " or p.open_type=1)";
        String sort = " order by p.id desc, pr.create_time desc";
        Session session = entityManager.unwrap(Session.class);
        NativeQuery<Map<String, Object>> query = session.createNativeQuery(select + from + where + sort + limitStr(pageable));
        query.addScalar("id", StandardBasicTypes.LONG);
        query.addScalar("projectTalentId", StandardBasicTypes.LONG);
        query.addScalar("projectId", StandardBasicTypes.LONG);
        query.addScalar("talentId", StandardBasicTypes.LONG);
        query.addScalar("followUserId", StandardBasicTypes.LONG);
        query.addScalar("talentStatus", StandardBasicTypes.INTEGER);
        query.addScalar("projectName", StandardBasicTypes.STRING);
        query.addScalar("talentName", StandardBasicTypes.STRING);
        query.addScalar("createUser", StandardBasicTypes.STRING);
        query.addScalar("createTime", StandardBasicTypes.TIMESTAMP);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        map.put("content", query.getResultList());
        map.put("totalElements", getTotal(countSelect + from + where));
        return map;
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
