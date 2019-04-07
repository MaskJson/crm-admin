package com.moving.admin.dao.natives;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectNative extends AbstractNative {

    private StringBuilder select = new StringBuilder("select a.id as id, a.name as name, u.nick_name as createUser, a.follow, a.create_time as createTime");
    private StringBuilder selectCount = new StringBuilder("select count(1)");
    private StringBuilder from = new StringBuilder(" from project a left join sys_user u on a.create_user_id=u.id");
    private StringBuilder where = new StringBuilder(" where 1=1");
    private StringBuilder sort = new StringBuilder(" order by ");

    private StringBuilder stringBuilder;

    public Map<String, Object> getResult(EntityManager entityManager) {
        Map<String, Object> map = new HashMap<>();
        map.put("content", getProjectList(entityManager));
        map.put("totalElements", getProjectTotalElements(entityManager));
        return map;
    }

    public List<Map<String, Object>> getProjectList(EntityManager entityManager) {
        String sql = select.append(from).append(where).append(sort).toString();
        Session session = entityManager.unwrap(Session.class);
        NativeQuery<Map<String, Object>> query = session.createNativeQuery(sql);
        query.addScalar("id", StandardBasicTypes.LONG);
        query.addScalar("name", StandardBasicTypes.STRING);
        query.addScalar("follow", StandardBasicTypes.BOOLEAN);
        query.addScalar("createUser", StandardBasicTypes.STRING);
        query.addScalar("createTime", StandardBasicTypes.DATE);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

    public BigInteger getProjectTotalElements(EntityManager entityManager) {
        String sqlCount = selectCount.append(from).append(where).append(sort).toString();
        Query query = entityManager.createNativeQuery(sqlCount);

        return objectToBigInteger(query.getSingleResult());
    }

    public void setFolder(String folderWhere) {
        where.append(" and " + folderWhere);
    }

    public void setCustomer(Long customerId) {
        where.append(" and a.customer_id=" + customerId);
    }

    public void setTeam(Long teamId) {
        where.append(" and a.team_id=" + teamId);
    }

    public void setIndustry(String industry) {
        where.append(" and a.industry like '%" + industry + "%'");
    }

    public void setCity(String city) {
        where.append(" and a.city='" + city + "'");
    }

    public void appendSort(Pageable pageable) {
        super.simpleAppendSort(pageable, sort);
    }
}
