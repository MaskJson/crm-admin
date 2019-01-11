package com.moving.admin.dao.natives;

import com.moving.admin.dao.natives.AbstractNative;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public class TestNative extends AbstractNative<Object> {
    private StringBuilder count = new StringBuilder("SELECT count(1)");
    private StringBuilder selectStr = new StringBuilder("SELECT a.id, a.order_name as orderName, a.price as orderPrice, b.nick_name as nickName");
    private StringBuilder fromStr = new StringBuilder(" FROM orders a LEFT JOIN user b ON a.user_id = b.id ");
    private StringBuilder whereStr = new StringBuilder(" WHERE 1=1 ");
    private StringBuilder sortStr = new StringBuilder(" ORDER BY ");
    public List<Map<String, Object>> getList(EntityManager entityManager) {
        String sql = selectStr.append(fromStr).append(whereStr).append(sortStr).toString();
        Session session = entityManager.unwrap(Session.class);
        @SuppressWarnings("unchecked")
		NativeQuery<Map<String, Object>> query = session.createNativeQuery(sql);
        query.addScalar("id", StandardBasicTypes.LONG);
        query.addScalar("orderName", StandardBasicTypes.STRING);
        query.addScalar("orderPrice", StandardBasicTypes.LONG);
        query.addScalar("nickName", StandardBasicTypes.STRING);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

    public BigInteger getTotal(EntityManager entityManager) {
        String sql = count.append(fromStr).append(whereStr).toString();
        Query query = entityManager.createNativeQuery(sql);
        return objectToBigInteger(query.getSingleResult());
    }

    public void setUserId(Long userId) {
        if (null != userId) {
            whereStr.append(" and a.user_id = ").append(userId);
        }
    }

    public void appendSort(Pageable pageable) {
        super.simpleAppendSort(pageable, sortStr);
    }

}
