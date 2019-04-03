package com.moving.admin.dao.natives;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CustomerNative extends BaseNative {

    private String contactSelect = "select c.id, c.name, d.name as department, c.position, c.important, c.phone, u.nick_name as createUser";
    private String contactFrom = " from customer_contact c left join department d on c.department_id = d.id left join sys_user u on u.id = c.create_user_id";
    private String contactWhere = " where c.customer_id = ";
    private String contactOrder = " order by c.id desc";

    private String remarkSelect = "select c.id, c.remark, c.create_time as createTime, u.nick_name as createUser";
    private String remarkFrom = " from customer_contact_remark c from sys_user u on c.create_user_id = u.id";
    private String remarkWhere = " where c.customer_contact_id = ";
    private String remarkOrder = " order by c.id desc";

    // 获取客户下所有联系人及联系记录
    public List<Map<String, Object>> getAllCustomerContactById(Long customerId) {
        String sql = contactSelect + contactFrom + contactWhere + customerId.toString() + contactOrder;
        Session session = entityManager.unwrap(Session.class);
        NativeQuery<Map<String, Object>> query = session.createNativeQuery(sql);
        query.addScalar("id", StandardBasicTypes.LONG);
        query.addScalar("name", StandardBasicTypes.STRING);
        query.addScalar("department", StandardBasicTypes.STRING);
        query.addScalar("position", StandardBasicTypes.STRING);
        query.addScalar("important", StandardBasicTypes.INTEGER);
        query.addScalar("phone", StandardBasicTypes.STRING);
        query.addScalar("createUser", StandardBasicTypes.STRING);
        List<Map<String, Object>> list = query.getResultList();
//        list.forEach(item -> {
//            item.put("remarks", getRemarkByContactId(Long.getLong(item.get("id").toString())));
//        });
        return list;
    }

    // 获取联系人下的联系记录
    public List<Map<String, Object>> getRemarkByContactId(Long contactId) {
        String sql = remarkSelect + remarkFrom + remarkWhere + contactId.toString() + remarkOrder;
        Session session = entityManager.unwrap(Session.class);
        NativeQuery<Map<String, Object>> query = session.createNativeQuery(sql);
        query.addScalar("id", StandardBasicTypes.LONG);
        query.addScalar("remark", StandardBasicTypes.STRING);
        query.addScalar("createTime", StandardBasicTypes.DATE);
        query.addScalar("createUser", StandardBasicTypes.STRING);
        return query.getResultList();
    }

}
