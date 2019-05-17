package com.moving.admin.dao.natives;

import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import com.moving.admin.dao.project.ProjectTalentDao;
import com.moving.admin.dao.sys.TeamDao;
import com.moving.admin.entity.customer.Customer;
import com.moving.admin.entity.sys.Team;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerNative extends AbstractNative {

    @Autowired
    private TeamDao teamDao;

    @Autowired
    private CountNative countNative;

    @Autowired
    private ProjectTalentDao projectTalentDao;

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

    // 获取当前项目总监相关的待审核的公司, 管理员可查看所有
    public Map<String, Object> getAuditList(Long userId, Long roleId, Pageable pageable) {
        Map<String, Object> map = new HashMap<>();
        Team team = teamDao.findTeamByUserIdAndLevel(userId, 1);

        if (team == null && roleId == 3) {
            map.put("content", new ArrayList<>());
            map.put("totalElements", 0);
            return map;
        }
        String teamWhere = "";
        if (team != null) {
            teamWhere =  "or c.create_user_id in(select user_id from team where team_id=" + team.getId() + ")";
        }
        String select = "select c.id, c.name, c.create_time as createTime, c.audit_type as auditType, u.nick_name as createUser";
        String countSelect = "select count(1)";
        String from = " from customer c left join sys_user u on c.create_user_id=u.id";
        String where = " where c.audit_type<>2 and (c.create_user_id="+userId+teamWhere+") ";
        String sort = " order by audit_type asc";
        if (roleId == 1) {
            where = " where c.audit_type<>2";
        }
        Session session = entityManager.unwrap(Session.class);
        NativeQuery<Map<String, Object>> query = session.createNativeQuery(select + from + where + sort + limitStr(pageable));
        query.addScalar("id", StandardBasicTypes.LONG);
        query.addScalar("name", StandardBasicTypes.STRING);
        query.addScalar("createUser", StandardBasicTypes.STRING);
        query.addScalar("createTime", StandardBasicTypes.TIMESTAMP);
        query.addScalar("auditType", StandardBasicTypes.INTEGER);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        map.put("content", query.getResultList());
        map.put("totalElements", getTotal(countSelect + from + where));
        return map;
    }

    // 获取公司人才库
    public List<Map<String, Object>> getTalentsByCustomerId(Long id) {
        String sql = "select e.talent_id as talentId, e.position, t.status, t.type as talentType, t.name as talentName, t.follow_user_id as followUserId, e.department_id as departmentId, d.name as department" +
                " from experience e left join department d on e.department_id=d.id left join talent t on e.talent_id=t.id" +
                " where e.customer_id=" + id +
                " order by e.department_id";
        Session session = entityManager.unwrap(Session.class);
        NativeQuery<Map<String, Object>> query = session.createNativeQuery(sql);
        query.addScalar("talentId", StandardBasicTypes.LONG);
        query.addScalar("followUserId", StandardBasicTypes.LONG);
        query.addScalar("position", StandardBasicTypes.STRING);
        query.addScalar("department", StandardBasicTypes.STRING);
        query.addScalar("talentName", StandardBasicTypes.STRING);
        query.addScalar("status", StandardBasicTypes.INTEGER);
        query.addScalar("talentType", StandardBasicTypes.INTEGER);
        query.addScalar("departmentId", StandardBasicTypes.LONG);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.getResultList();
        list.forEach(item -> {
            Long talentId = Long.parseLong(item.get("talentId").toString());
            item.put("remind", countNative.getRemindInfo(talentId));
            item.put("progress", projectTalentDao.getProjectLengthByTalentId(talentId));
            item.put("projects", projectTalentDao.findProjectIdsOfTalent(talentId));
            item.put("offerCount", projectTalentDao.getProjectOfferLength(talentId));
        });
        return list;
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
