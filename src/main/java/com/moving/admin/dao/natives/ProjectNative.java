package com.moving.admin.dao.natives;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProjectNative extends AbstractNative {

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
        String select = "select pt.id, pt.type, pt.status, pt.update_time as updateTime, p.id as projectId p.name as projectName, c.name as customerName";
        String whereFrom = " from project_talent pt left join project p on pt.project_id = p.id left join customer c on p.customer_id=c.id" +
                " where pt.talent_id=" + talentId;
        String sort = " order by pt.update_time desc";
        Session session = entityManager.unwrap(Session.class);
        NativeQuery<Map<String, Object>> query = session.createNativeQuery(select + whereFrom + sort);
        query.addScalar("id", StandardBasicTypes.LONG);
        query.addScalar("projectId", StandardBasicTypes.LONG);
        query.addScalar("projectName", StandardBasicTypes.STRING);
        query.addScalar("customerName", StandardBasicTypes.STRING);
        query.addScalar("type", StandardBasicTypes.INTEGER);
        query.addScalar("status", StandardBasicTypes.INTEGER);
        query.addScalar("updateTime", StandardBasicTypes.TIMESTAMP);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

    // 获取对当前用户开放的所有项目
    public List<Map<String, Object>> getProjectsByUser(Long userId) {
        String select = "select a.id, a.name, c.name as customerName";
        String from = " from project a left join customer c on a.customer_id=c.id";
        String where = " where a.create_user_id=" + userId + " or a.advise_id="+userId+" or (a.open_type=1 and date_add(a.create_time, interval 7 day) < now()) or a.part_id=" + userId +
                " or (" + userId + " in (select ttt.user_id from team ttt where ttt.team_id=a.team_id and ttt.team_id is not null) and a.open_type=2)";
        Session session = entityManager.unwrap(Session.class);
        NativeQuery<Map<String, Object>> query = session.createNativeQuery(select + from + where);
        query.addScalar("id", StandardBasicTypes.LONG);
        query.addScalar("name", StandardBasicTypes.STRING);
        query.addScalar("customerName", StandardBasicTypes.STRING);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

    public void appendSort(Pageable pageable) {}
}
