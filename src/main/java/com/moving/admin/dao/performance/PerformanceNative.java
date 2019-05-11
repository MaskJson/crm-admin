package com.moving.admin.dao.performance;

import com.moving.admin.dao.natives.AbstractNative;
import com.moving.admin.dao.project.ProjectTalentDao;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PerformanceNative extends AbstractNative {

    @Autowired
    private ProjectTalentDao projectTalentDao;

    private String select = "select pr.status as remindStatus, pt.id, pt.project_id as projectId, pt.talent_id as talentId, pt.status, pt.type, pt.create_time as createTime, pt.update_time as updateTime," +
            " p.name as projectName, t.name as talentName, t.status as talentStatus, t.type as talentType, t.follow_user_id as followuserId, c.id as customerId, c.name as customerName," +
            " d.id as departmentId, d.name as departmentName";
    private String from = " from project_remind pr left join project_talent pt on pr.project_talent_id=pt.id " +
            " left join project p on pt.project_id=p.id left join talent t on pt.talent_id=t.id" +
            " left join customer c on p.customer_id=c.id left join department d on p.department_id=d.id" +
            " left join sys_user u on u.id=pt.create_user_id";
    private String where = " where pt.create_user_id=";
    private String sort = " group by pr.project_talent_id, pr.status order by c.id,p.id, pt.status, pt.update_time desc ";

    // 日、周、月绩效
    public List<Map<String, Object>> getPerformance(Long userId, Integer flag, String time) {
        String whereStr = "";
        switch (flag) {
            case 1: whereStr = " and to_days(pr.create_time) = to_days(time)";break;
            case 2: whereStr = " and YEARWEEK(date_format(pr.create_time)) = YEARWEEK(time)";break;
            case 3: whereStr = " and DATE_FORMAT(pr.create_time, '%Y%m') = DATE_FORMAT(time, '%Y%m')";break;
        }
        return getProjectStatusTalents(where + whereStr);
    }
    // 周、月报
    public void getPerformanceReport(Long userId, Long roleId, String time) {

    }

    public List<Map<String, Object>> getProjectStatusTalents(String where) {
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

    public void appendSort(Pageable pageable) {

    }
}
