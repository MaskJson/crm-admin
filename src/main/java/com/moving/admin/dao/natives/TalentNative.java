package com.moving.admin.dao.natives;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TalentNative extends AbstractNative {

    public List<Long> getTalentIds(String customerName) {
        List<Long> longs = new ArrayList<>();
        String sql = "select talent_id as id" +
                " from experience" +
                " where e.customer_id in (select id from customer where name like '%"+customerName+"%')";
        Session session = entityManager.unwrap(Session.class);
        NativeQuery<Map<String, Object>> query = session.createNativeQuery(sql);
        query.addScalar("id", StandardBasicTypes.LONG);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        query.getResultList().forEach(item -> {
            longs.add(Long.parseLong(item.get("id").toString()));
        });
        return longs;
    }

    public void appendSort(Pageable pageable) {

    }
}
