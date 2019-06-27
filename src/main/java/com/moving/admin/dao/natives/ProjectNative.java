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
public class ProjectNative extends AbstractNative {

    @Autowired
    private ProjectTalentDao projectTalentDao;

    // 获取项目的诊断报告记录
    public List<Map<String, Object>> getDiagnosisByProjectId(Long projectId) {
        String sql = "select rp.id as id, u.nick_name as userName, rp.create_time as createTime, rp.remark as remark";
        String whereFrom = " from project_report rp left join sys_user u on rp.create_user_id=u.id where rp.project_id=";
        String sort = " order by rp.create_time desc";
        Session session = entityManager.unwrap(Session.class);
        NativeQuery<Map<String, Object>> query = session.createNativeQuery(sql + whereFrom + projectId + sort);
        query.addScalar("id", StandardBasicTypes.LONG);
        query.addScalar("userName", StandardBasicTypes.STRING);
        query.addScalar("createTime", StandardBasicTypes.TIMESTAMP);
        query.addScalar("remark", StandardBasicTypes.STRING);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

    // 获取人才的项目经历
    public List<Map<String, Object>> getTalentProjects(Long talentId) {
        String select = "select pt.id, pt.create_user_id as createUserId,pt.recommendation, pt.kill_remark as killRemark,pt.probation_time as probationTime," +
                " pt.type, pt.status, pt.update_time as updateTime, pt.remark_status as remarkStatus, p.id as projectId, p.name as projectName," +
                "p.create_user_id as projectCreateUserId, c.name as customerName, u.nick_name as createUser";
        String whereFrom = " from project_talent pt left join project p on pt.project_id = p.id left join customer c on p.customer_id=c.id left join sys_user u on u.id=pt.create_user_id" +
                " where pt.talent_id=" + talentId;
        String sort = " order by c.name, p.name, pt.update_time desc";
        Session session = entityManager.unwrap(Session.class);
        NativeQuery<Map<String, Object>> query = session.createNativeQuery(select + whereFrom + sort);
        query.addScalar("id", StandardBasicTypes.LONG);
        query.addScalar("createUserId", StandardBasicTypes.LONG);
        query.addScalar("projectCreateUserId", StandardBasicTypes.LONG);
        query.addScalar("projectId", StandardBasicTypes.LONG);
        query.addScalar("projectName", StandardBasicTypes.STRING);
        query.addScalar("recommendation", StandardBasicTypes.STRING);
        query.addScalar("killRemark", StandardBasicTypes.STRING);
        query.addScalar("customerName", StandardBasicTypes.STRING);
        query.addScalar("createUser", StandardBasicTypes.STRING);
        query.addScalar("type", StandardBasicTypes.INTEGER);
        query.addScalar("status", StandardBasicTypes.INTEGER);
        query.addScalar("updateTime", StandardBasicTypes.TIMESTAMP);
        query.addScalar("probationTime", StandardBasicTypes.TIMESTAMP);
        query.addScalar("remarkStatus", StandardBasicTypes.INTEGER);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.getResultList();
        list.forEach(item -> {
            item.put("reminds", getProjectRemindByProjectTalentId(Long.parseLong(item.get("id").toString())));
        });
        return list;
    }

    // 获取该人才的项目进展跟踪记录
    public List<Map<String, Object>> getProjectRemindByProjectTalentId(Long id) {
        String sql = "select pr.status, pr.type, pr.create_time as createTime, pr.remark, u.nick_name as createUser," +
                "pr.recommendation,pr.kill_remark as killRemark,pr.interview_time as interviewTime,pr.interview_tone as interviewTone,pr.is_last as isLast," +
                "pr.position,pr.year_salary as yearSalary,pr.charge,pr.sure_time as sureTime,pr.work_time as workTime," +
                "pr.entry_time as entryTime,pr.probation_time as probationTime,pr.talent_remark as talentRemark,pr.customer_remark as customerRemark, pr.remark_status as remarkStatus" +
                " from project_remind pr left join sys_user u on u.id=pr.create_user_id" +
                " where project_talent_id=" + id +
                " order by pr.status desc, pr.create_time desc";
        Session session = entityManager.unwrap(Session.class);
        NativeQuery<Map<String, Object>> query = session.createNativeQuery(sql);
        query.addScalar("status", StandardBasicTypes.INTEGER);
        query.addScalar("type", StandardBasicTypes.INTEGER);
        query.addScalar("createTime", StandardBasicTypes.TIMESTAMP);
        query.addScalar("remark", StandardBasicTypes.STRING);
        query.addScalar("createUser", StandardBasicTypes.STRING);
        query.addScalar("recommendation", StandardBasicTypes.STRING);
        query.addScalar("killRemark", StandardBasicTypes.STRING);
        query.addScalar("interviewTone", StandardBasicTypes.STRING);
        query.addScalar("isLast", StandardBasicTypes.BOOLEAN);
        query.addScalar("position", StandardBasicTypes.STRING);
        query.addScalar("yearSalary", StandardBasicTypes.DOUBLE);
        query.addScalar("charge", StandardBasicTypes.DOUBLE);
        query.addScalar("sureTime", StandardBasicTypes.TIMESTAMP);
        query.addScalar("interviewTime", StandardBasicTypes.TIMESTAMP);
        query.addScalar("workTime", StandardBasicTypes.TIMESTAMP);
        query.addScalar("entryTime", StandardBasicTypes.TIMESTAMP);
        query.addScalar("probationTime", StandardBasicTypes.TIMESTAMP);
        query.addScalar("talentRemark", StandardBasicTypes.STRING);
        query.addScalar("customerRemark", StandardBasicTypes.STRING);
        query.addScalar("remarkStatus", StandardBasicTypes.INTEGER);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

    // 获取对当前用户开放的所有项目
    public List<Map<String, Object>> getProjectsByUser(Long userId, Long roleId) {
        String select = "select a.id, a.name, c.name as customerName";
        String from = " from project a left join customer c on a.customer_id=c.id";
        String where = " where a.create_user_id=" + userId +
                " or  a.advise_id="+userId +
                " or "+userId+" in(select pa.user_id from project_adviser pa where pa.project_id=a.id)"+
                " or (" + userId + " in (select ttt.user_id from team ttt where ttt.team_id=a.team_id and ttt.team_id is not null) " +
                "and (a.open_type<>1 or (a.open_type=1 and date_add(a.create_time, interval 7 day) < now())))";
        // admin
        if (roleId == 1) {
            where = "";
        }
        Session session = entityManager.unwrap(Session.class);
        NativeQuery<Map<String, Object>> query = session.createNativeQuery(select + from + where);
        query.addScalar("id", StandardBasicTypes.LONG);
        query.addScalar("name", StandardBasicTypes.STRING);
        query.addScalar("customerName", StandardBasicTypes.STRING);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

    // 获取当前用户推荐的被淘汰的进展人才
    public Map<String, Object> getKillProjectTalents(Long userId, Pageable pageable) {
        Map<String, Object> map = new HashMap<>();
        String select = "select pt.id, p.id as projectId, p.name as projectName, t.id as talentId, t.follow_user_id as followUserId, t.type as talentType, t.name as talentName, pt.kill_remark as killRemark, pt.update_time as updateTime";
        String count = "select count(1) ";
        String sql = " from project_talent pt left join talent t on pt.talent_id=t.id left join project p on p.id=pt.project_id" +
                " where pt.status=8 and pt.create_user_id="+userId+" order by pt.update_time desc ";
        Session session = entityManager.unwrap(Session.class);
        NativeQuery<Map<String, Object>> query = session.createNativeQuery(select + sql + limitStr(pageable));
        query.addScalar("id", StandardBasicTypes.LONG);
        query.addScalar("projectId", StandardBasicTypes.LONG);
        query.addScalar("talentType", StandardBasicTypes.INTEGER);
        query.addScalar("followUserId", StandardBasicTypes.LONG);
        query.addScalar("projectName", StandardBasicTypes.STRING);
        query.addScalar("talentId", StandardBasicTypes.LONG);
        query.addScalar("talentName", StandardBasicTypes.STRING);
        query.addScalar("killRemark", StandardBasicTypes.STRING);
        query.addScalar("updateTime", StandardBasicTypes.TIMESTAMP);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.getResultList();
        list.forEach(item -> {
            Long id = Long.parseLong(item.get("talentId").toString());
            item.put("progress", projectTalentDao.getProjectLengthByTalentId(id));
        });
        map.put("content", list);
        map.put("totalElements", getTotal(count + sql));
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
