package com.moving.admin.dao.natives;

import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import com.moving.admin.entity.customer.Customer;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerNative extends AbstractNative {

    // 获取客户下所有联系人及联系记录
    public List<Map<String, Object>> getAllCustomerContactById(Long customerId, String name, Long departmentId, String position, String phone) {
        String contactSelect = "select c.id, c.name, d.name as department, c.create_user_id as createUserId, c.create_time as createTime, c.position, c.important, c.phone, u.nick_name as createUser";
        String contactFrom = " from customer_contact c left join department d on c.department_id = d.id left join sys_user u on u.id = c.create_user_id";
        String contactWhere = " where c.customer_id = ";
        String contactOrder = " order by c.id desc";
        // 处理查询条件
        String where = "";
        if (StringUtils.isNotEmpty(name)) {
            where = where + " and c.name = '" + name + "'";
        }
        if (departmentId != null) {
            where = where + " and c.department_id = " + departmentId;
        }
        if (StringUtils.isNotEmpty(position)) {
            where = where + " and c.position = '" + position + "'";
        }
        if (StringUtils.isNotEmpty(phone)) {
            where = where + " and c.phone = '" + phone + "'";
        }
        String sql = contactSelect + contactFrom + contactWhere + customerId + where + contactOrder;
        Session session = entityManager.unwrap(Session.class);
        NativeQuery<Map<String, Object>> query = session.createNativeQuery(sql);
        query.addScalar("id", StandardBasicTypes.LONG);
        query.addScalar("name", StandardBasicTypes.STRING);
        query.addScalar("department", StandardBasicTypes.STRING);
        query.addScalar("createUserId", StandardBasicTypes.LONG);
        query.addScalar("createTime", StandardBasicTypes.TIMESTAMP);
        query.addScalar("position", StandardBasicTypes.STRING);
        query.addScalar("important", StandardBasicTypes.INTEGER);
        query.addScalar("phone", StandardBasicTypes.STRING);
        query.addScalar("createUser", StandardBasicTypes.STRING);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.getResultList();
        list.forEach(item -> {
            item.put("remarks", getRemarkByContactId((long)item.get("id")));
        });
        return list;
    }

    // 获取联系人下的联系记录
    public List<Map<String, Object>> getRemarkByContactId(Long contactId) {
        String remarkSelect = "select c.id, c.remark, c.create_time as createTime, u.nick_name as createUser";
        String remarkFrom = " from customer_contact_remark c join sys_user u on c.create_user_id = u.id";
        String remarkWhere = " where c.customer_contact_id = ";
        String remarkOrder = " order by c.id desc";
        String sql = remarkSelect + remarkFrom + remarkWhere + contactId + remarkOrder;
        Session session = entityManager.unwrap(Session.class);
        NativeQuery<Map<String, Object>> query = session.createNativeQuery(sql);
        query.addScalar("id", StandardBasicTypes.LONG);
        query.addScalar("remark", StandardBasicTypes.STRING);
        query.addScalar("createTime", StandardBasicTypes.TIMESTAMP);
        query.addScalar("createUser", StandardBasicTypes.STRING);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

    // 获取公司下项目
    public Map<String, Object> getProjectByCustomerId(Long customerId, Pageable pageable) {
        Map<String, Object> map = new HashMap<>();
        String select = "select p.id, p.name as projectName, u.nick_name as createUser, p.create_time as createTime";
        String countSelect = "select count(1)";
        String from = " from project p left join sys_user u on p.create_user_id=u.id";
        String where = " where p.customer_id=" + customerId;
        String sort = " order by p.create_time desc";
        Session session = entityManager.unwrap(Session.class);
        NativeQuery<Map<String, Object>> query = session.createNativeQuery(select + from + where + sort + limitStr(pageable));
        query.addScalar("id", StandardBasicTypes.LONG);
        query.addScalar("projectName", StandardBasicTypes.STRING);
        query.addScalar("createUser", StandardBasicTypes.STRING);
        query.addScalar("createTime", StandardBasicTypes.TIMESTAMP);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        map.put("content", query.getResultList());
        map.put("totalElements", getTotal(countSelect + from + where));
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

    public void appendSort(Pageable pageable) {

    }

}
