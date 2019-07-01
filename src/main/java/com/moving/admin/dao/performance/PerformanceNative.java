package com.moving.admin.dao.performance;

import com.moving.admin.dao.natives.AbstractNative;
import com.moving.admin.dao.natives.CountNative;
import com.moving.admin.dao.project.ProjectRemindDao;
import com.moving.admin.dao.project.ProjectTalentDao;
import com.moving.admin.entity.project.ProjectRemind;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class PerformanceNative extends AbstractNative {

    @Autowired
    private ProjectTalentDao projectTalentDao;

    @Autowired
    private ProjectRemindDao projectRemindDao;

    private final String select = "select pr.id as remindId, pr.status as remindStatus, pt.id, pt.project_id as projectId, pt.talent_id as talentId, pt.status, pt.type, pt.create_time as createTime, pt.update_time as updateTime," +
            " p.name as projectName, t.name as talentName, t.status as talentStatus, t.type as talentType, t.follow_user_id as followuserId, c.id as customerId, c.name as customerName," +
            " d.id as departmentId, d.name as departmentName, pr.create_user_id as createUserId, u.nick_name as createUser";
    private final String from = " from project_remind pr left join project_talent pt on pr.project_talent_id=pt.id " +
            " left join project p on pt.project_id=p.id left join talent t on pt.talent_id=t.id" +
            " left join customer c on p.customer_id=c.id left join department d on p.department_id=d.id" +
            " left join sys_user u on u.id=pt.create_user_id";
    private final String where = " where pr.create_user_id=";
    private final String sort = " group by pr.project_talent_id, pr.status order by c.id,p.id, pt.status, pt.update_time desc ";
    private final String statusWhere = " and (pr.status <> 0 || pr.type=100) and pr.status <> 8";

    //进展跟踪 日、周、月绩效
    public List<Map<String, Object>> getPerformance(Long userId, Integer flag, String time) {
        String whereStr = "";
        if (StringUtils.isEmpty(time)) {
            time = "now()";
        } else {
            time = "'"+time+"'";
        }
        switch (flag) {
            case 1: whereStr = " and to_days(pr.create_time) = to_days("+time+")";break;
            case 2: whereStr = " and YEARWEEK(date_format(pr.create_time, '%Y-%m-%d')) = YEARWEEK("+time+", '%Y-%m-%d')";break;
            case 3: whereStr = " and DATE_FORMAT(pr.create_time, '%Y%m') = "+time;break;
        }
        return getProjectStatusTalents(where + userId + whereStr + statusWhere);
    }
    //进展日、 周、月报表
    public List<Map<String, Object>> getPerformanceReport(Long userId, Long roleId, Integer flag, String time, Long memberId) {
        String where = "";
        if (StringUtils.isEmpty(time)) {
            time = "now()";
        } else {
            time = "'"+time+"'";
        }
        String levelFilter = flag == 1 ? "" : " level is null and ";
        if (memberId != null) {
            where = " where pr.create_user_id=" + memberId;
        } else {
            switch (Integer.parseInt(roleId.toString())) {
                case 2:
                case 6:
                case 7: where = " where pr.create_user_id in(select user_id from team where "+levelFilter+" parent_id in(select id from team where level in(2,3,4) and user_id="+userId+"))";break;
                case 3: where = " where pr.create_user_id in (select user_id from team where team_id in(select id from team where level=1 and user_id="+userId+"))";break;
                case 1: where = " where pr.create_user_id in (select user_id from team where level=1)";break;
            }
        }
        switch (flag) {
            case 1:where = where + " and to_days(pr.create_time) = to_days("+time+")";break;
            case 2:where = where + " and YEARWEEK(date_format(pr.create_time, '%Y-%m-%d')) = YEARWEEK("+time+", '%Y-%m-%d')";break;
            case 3:where = where + " and DATE_FORMAT(pr.create_time, '%Y%m') = "+time;break;
        }
        return getProjectStatusTalents(where + statusWhere);
    }

    public List<Map<String, Object>> getProjectStatusTalents(String where) {
        List<Map<String, Object>> talentList = getList(where);
        talentList.forEach(map -> {
            Long id = Long.parseLong(map.get("talentId").toString());
            map.put("reminds", projectRemindDao.findAllByProjectTalentIdOrderByCreateTimeDesc(Long.parseLong(map.get("id").toString())));
            List<ProjectRemind> reminds = projectRemindDao.findAllById(Long.parseLong(map.get("remindId").toString()));
            map.put("remind", reminds.size() > 0 ? reminds.get(0) : new HashMap<>());
            map.put("progress", projectTalentDao.getProjectLengthByTalentId(id));
            map.put("projects", projectTalentDao.findProjectIdsOfTalent(id));
            map.put("offerCount", projectTalentDao.getProjectOfferLength(id));
        });
        return talentList;
    }

    public List<Map<String, Object>> getInterview(Long userId, Long roleId) {
        String idStr = "1=1";
        if (roleId == null) {
            idStr = "pr.create_user_id=" + userId;
        } else if (roleId == 1) {
            idStr = "1=1";
        } else if (roleId == 3) {
            idStr = "pr.create_user_id in (select user_id from team where team_id in (select id from team where user_id="+userId+" and level=1))";
        }
        String where = " where " + idStr + " and (pr.type=2 or pr.type=4 or pr.type=8 or pr.remark_status=2) and " +
                "to_days(pr.interview_time) = to_days(now()) and now()<pr.interview_time";
        List<Map<String, Object>> list = getList(where);
        list.forEach(map -> {
            List<ProjectRemind> reminds = projectRemindDao.findAllById(Long.parseLong(map.get("remindId").toString()));
            map.put("remind", reminds.size() > 0 ? reminds.get(0) : new HashMap<>());
        });
        return list;
    }

    public List<Map<String, Object>> getList(String where) {
        Session session = entityManager.unwrap(Session.class);
        NativeQuery<Map<String, Object>> query = session.createNativeQuery(select + from + where + sort);
        query.addScalar("remindStatus", StandardBasicTypes.INTEGER);
        query.addScalar("id", StandardBasicTypes.LONG);
        query.addScalar("createUserId", StandardBasicTypes.LONG);
        query.addScalar("createUser", StandardBasicTypes.STRING);
        query.addScalar("remindId", StandardBasicTypes.LONG);
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
        return talentList;
    }

    public void appendSort(Pageable pageable) {

    }
}
