package com.moving.admin.dao.natives;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// 联调所需数据
@Service
public class AdjustNative extends AbstractNative {

    @Autowired
    private CountNative countNative;

    private String talentSelect = "select pt.id as id, t.id as talentId, t.name as name, t.city as city, " +
                                     "t.salary as salary, t.phone as phone, t.tag as tag, t.status as status, pt.type as type, pt.update_time as updateTime";
    private String talentFrom = " from project_talent pt left join talent t on pt.talent_id=t.id left join project_remind pr on pr.project_id=pt.project_id";
    private String talentWhere = " where pt.status=";
    private String talentSort = " order by pt.update_time desc";

    // 根据状态获取项目人才
    public List<Map<String, Object>> getProjectTalent(Integer status, Long projectId, Long userId) {
        String sql = talentSelect + talentFrom + talentWhere + status + " and pt.project_id=" + projectId + " and pr.create_user_id=" + userId + talentSort;
        Session session = entityManager.unwrap(Session.class);
        NativeQuery<Map<String, Object>> query = session.createNativeQuery(sql);
        query.addScalar("id", StandardBasicTypes.LONG);
        query.addScalar("talentId", StandardBasicTypes.LONG);
        query.addScalar("name", StandardBasicTypes.STRING);
        query.addScalar("city", StandardBasicTypes.STRING);
        query.addScalar("salary", StandardBasicTypes.STRING);
        query.addScalar("phone", StandardBasicTypes.STRING);
        query.addScalar("tag", StandardBasicTypes.STRING);
        query.addScalar("status", StandardBasicTypes.INTEGER);
        query.addScalar("type", StandardBasicTypes.INTEGER);
        query.addScalar("updateTime", StandardBasicTypes.TIMESTAMP);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.getResultList();
        list.forEach(item -> {
            item.put("position", countNative.getWorkInfo(Long.parseLong(item.get("id").toString())).get("position"));
        });
        return query.getResultList();
    }

    // 获取该项目已关联的人才
    public List<Long> getTalentsByProjectId(Long projectId) {
        String sql = "select talent_id as id  from project_talent where project_id=" + projectId + " and status<7";
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
