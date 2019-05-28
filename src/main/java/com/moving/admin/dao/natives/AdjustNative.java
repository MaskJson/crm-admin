package com.moving.admin.dao.natives;

import com.moving.admin.dao.project.ProjectRemindDao;
import com.moving.admin.entity.project.ProjectRemind;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 联调所需数据
@Service
public class AdjustNative extends AbstractNative {

    @Autowired
    private ProjectRemindDao projectRemindDao;

    private String talentSelect = "select pt.id as id, pt.talent_id as talentId, pt.create_user_id as createUserId, pt.probation_time as probationTime, t.name as name, t.name as talentName," +
                                     "t.phone as phone, pt.type as type, pt.status, pt.update_time as updateTime, pt.recommendation, pt.kill_remark as killRemark,pt.remark_status as remarkStatus," +
                                     "p.name as projectName,p.id as projectId, c.name as customerName";
    private String talentFrom = " from project_talent pt left join talent t on pt.talent_id=t.id left join project p on p.id=pt.project_id left join customer c on c.id=p.customer_id";
    private String talentWhere = " where pt.status=";
    private String talentSort = " order by pt.update_time asc";

    // 根据状态获取项目人才, 当projectId 为null时，获取所有项目的
    public List<Map<String, Object>> getProjectTalent(Integer status, Long projectId, Long userId) {
        String sql = talentSelect + talentFrom + (status != null ? (talentWhere +(status == 1 ? 0 + " or pt.status=1" : status)) : " where 1=1")
                + (projectId != null ? (" and pt.project_id=" + projectId) : "" ) + " and (pt.create_user_id=" + userId +
                " or p.create_user_id="+userId+" )" +
                talentSort;
        Session session = entityManager.unwrap(Session.class);
        NativeQuery<Map<String, Object>> query = session.createNativeQuery(sql);
        query.addScalar("id", StandardBasicTypes.LONG);
        query.addScalar("talentId", StandardBasicTypes.LONG);
        query.addScalar("projectId", StandardBasicTypes.LONG);
        query.addScalar("createUserId", StandardBasicTypes.LONG);
        query.addScalar("name", StandardBasicTypes.STRING);
        query.addScalar("talentName", StandardBasicTypes.STRING);
        query.addScalar("recommendation", StandardBasicTypes.STRING);
        query.addScalar("killRemark", StandardBasicTypes.STRING);
        query.addScalar("phone", StandardBasicTypes.STRING);
        query.addScalar("projectName", StandardBasicTypes.STRING);
        query.addScalar("customerName", StandardBasicTypes.STRING);
        query.addScalar("type", StandardBasicTypes.INTEGER);
        query.addScalar("status", StandardBasicTypes.INTEGER);
        query.addScalar("updateTime", StandardBasicTypes.TIMESTAMP);
        query.addScalar("remarkStatus", StandardBasicTypes.INTEGER);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.getResultList();
        list.forEach(item -> {
//            item.put("position", countNative.getWorkInfo(Long.parseLong(item.get("talentId").toString())).get("position"));
            item.put("reminds", getLastRemindByStatus(Long.parseLong(item.get("id").toString())));
        });
        return list;
    }

    // 获取进展人才当前状态下的跟踪
    public List<ProjectRemind> getLastRemindByStatus(Long projectTalentId) {
        return projectRemindDao.findAllByProjectTalentIdOrderByCreateTimeDesc(projectTalentId);
    }

    // 获取该项目已关联的人才
    public List<Long> getTalentsByProjectId(Long projectId) {
        String sql = "select talent_id as id  from project_talent where project_id=" + projectId + " and status<8";
        Session session = entityManager.unwrap(Session.class);
        NativeQuery<Map<String, Object>> query = session.createNativeQuery(sql);
        query.addScalar("id", StandardBasicTypes.LONG);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.getResultList();
        List<Long> result = new ArrayList<>();
        list.forEach(map -> {
            result.add(Long.parseLong(map.get("id").toString()));
        });
        return result;
    }

    public void appendSort(Pageable pageable) {}
}
