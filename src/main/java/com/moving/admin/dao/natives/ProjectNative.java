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
        query.addScalar("createTime", StandardBasicTypes.DATE);
        query.addScalar("remark", StandardBasicTypes.STRING);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

    public void appendSort(Pageable pageable) {}
}
