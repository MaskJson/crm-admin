package com.moving.admin.dao.natives;

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
public class HomePageNative extends AbstractNative {

    // 统计人才和客户的跟踪待办
    public Map<String, BigInteger> homeCountRemind(Long userId) {
        Map<String, BigInteger> map = new HashMap<>();
        String countSelectTal = "select count(1)";
        String fromTal = " from talent_remind r left join talent t on r.talent_id=t.id";
        String whereTal = " where r.create_user_id=" + userId +" and r.finish=0";
        String countSelectCust = "select count(1)";
        String fromCust  = " from customer_remind r left join customer c on r.customer_id=c.id";
        String whereCust = " where r.create_user_id=" + userId + " and r.finish=0";
        map.put("talentFirst", getTotal(countSelectTal + fromTal + whereTal + " and r.type=1"));
        map.put("talentSecond", getTotal(countSelectTal + fromTal + whereTal + " and r.type=2"));
        map.put("talentThird", getTotal(countSelectTal + fromTal + whereTal + " and r.type=3"));
        map.put("customerFirst", getTotal(countSelectCust + fromCust + whereCust + " and r.type=1"));
        map.put("customerSecond", getTotal(countSelectCust + fromCust + whereCust + " and r.type=2"));
        map.put("customerThird", getTotal(countSelectCust + fromCust + whereCust + " and r.type=3"));
        return map;
    }

    // 获取项目进展提醒的列表
    public Map<String, Object> talentProgressList(Long userId, Long roleId, Integer type, Integer status, Pageable pageable) {
        Map<String, Object> map = new HashMap<>();
        String countSelect = "select count(1)";
        String select = "select pt.id, t.id as talentId, t.follow_user_id as followUserId, p.id as projectId, t.name as talentName, p.name as projectName, " +
                "u.nick_name as createUser, pt.status as projectStatus, t.status as talentStatus, pt.create_time as createTime" +
                ", p.first_apply_time as firstApplyTime, p.finish_time as finishTime";
        String fromWhere = getCountProjectFrom() + getTeamMemberSql(roleId, userId);
        String sort = " order by p.id desc, pt.status asc, pt.create_time asc";
        if (type == 1) {
            select = select + ", (select count(1) from talent_remind tr where pt.talent_id=tr.talent_id and date_add(tr.create_time, interval 3 day)>now()) as remindCount ";
        }
        switch (type) {
            case 1: fromWhere = fromWhere + qiDong(status, userId); break;
            case 2: fromWhere = fromWhere + gongJian(status); break;
            case 3: fromWhere = fromWhere + shouWei(status); break;
            case 4: fromWhere = fromWhere + jieShu(status); break;
        }
        Session session = entityManager.unwrap(Session.class);
        NativeQuery<Map<String, Object>> query = session.createNativeQuery(select + fromWhere + sort + limitStr(pageable));
        query.addScalar("id", StandardBasicTypes.LONG);
        query.addScalar("talentId", StandardBasicTypes.LONG);
        query.addScalar("followUserId", StandardBasicTypes.LONG);
        query.addScalar("projectId", StandardBasicTypes.LONG);
        query.addScalar("talentName", StandardBasicTypes.STRING);
        query.addScalar("projectName", StandardBasicTypes.STRING);
        query.addScalar("createUser", StandardBasicTypes.STRING);
        query.addScalar("projectStatus", StandardBasicTypes.INTEGER);
        query.addScalar("talentStatus", StandardBasicTypes.INTEGER);
        query.addScalar("createTime", StandardBasicTypes.TIMESTAMP);
        query.addScalar("firstApplyTime", StandardBasicTypes.TIMESTAMP);
        query.addScalar("finishTime", StandardBasicTypes.TIMESTAMP);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        map.put("content", query.getResultList());
        map.put("totalElements", getTotal(countSelect + fromWhere));
        return map;
    }

    // 统计项目进展，四大阶段的相关人才数量
    public Map<String, BigInteger> homeCountProjectTalent(Map<String, BigInteger> map, Long userId, Long roleId) {
        String countSelect = "select count(1)";
        map.put("qiDongFirst", getTotal(countSelect + getCountProjectFrom() + getTeamMemberSql(roleId, userId) + qiDong(1, userId)));
        map.put("qiDongSecond", getTotal(countSelect + getCountProjectFrom() + getTeamMemberSql(roleId, userId) + qiDong(2, userId)));
        map.put("qiDongThird", getTotal(countSelect + getCountProjectFrom() + getTeamMemberSql(roleId, userId) + qiDong(3, userId)));
        map.put("gongJianFirst", getTotal(countSelect + getCountProjectFrom() + getTeamMemberSql(roleId, userId) + gongJian(1)));
        map.put("gongJianSecond", getTotal(countSelect + getCountProjectFrom() + getTeamMemberSql(roleId, userId) + gongJian(2)));
        map.put("shouWeiFirst", getTotal(countSelect + getCountProjectFrom() + getTeamMemberSql(roleId, userId) + shouWei(4)));
        map.put("shouWeiSecond", getTotal(countSelect + getCountProjectFrom() + getTeamMemberSql(roleId, userId) + shouWei(5)));
        map.put("shouWeiThird", getTotal(countSelect + getCountProjectFrom() + getTeamMemberSql(roleId, userId) + shouWei(6)));
        map.put("jieShuFirst", getTotal(countSelect + getCountProjectFrom() + getTeamMemberSql(roleId, userId) + jieShu(7)));
        map.put("jieShuSecond", getTotal(countSelect + getCountProjectFrom() + getTeamMemberSql(roleId, userId) + jieShu(8)));
        return map;
    }

    public String getCountProjectFrom() {
        return " from project_talent pt left join project p on pt.project_id=p.id left join talent t on t.id=pt.talent_id left join sys_user u on u.id=pt.create_user_id";
    }

    // 获取项目进展阶段成员过滤
    public String getTeamMemberSql(Long roleId, Long userId) {
        String whereStart = " where p.team_id in (select " + (roleId == 3 ? "id" : "team_id") +" from team where user_id=" + userId + ") and" +
                " pt.create_user_id in(select user_id from team where level " + (roleId == 3 ? "in(2,3,4,5))" : "is null)");
        return whereStart;
    }

    //////// 获取查询条件
    //// 启动阶段
    public String qiDong(Integer status, Long userId) {
        if (status == null) {
            return " and pt.status=0";
        } else {
            switch (status) {
                case 1:
                case 2: return " and pt.status=0 and p.first_apply_time>now() and (select count(1) from talent_remind tr " +
                        " where pt.talent_id=tr.talent_id and date_add(tr.create_time, interval 3 day)>now())" + (status==1 ? ">0":"<1");
                case 3: return "and p.first_apply_time<now()";
                default: return " and pt.status=0";
            }
        }
    }

    // 攻坚阶段，推荐成功，且未淘汰，未超过计划完成时间
    public String gongJian(Integer status) {
        if (status == null) {
            return " and pt.status>0 and pt.status<4";
        } else {
            if (status == 1) {
                return " and p.finish_time>now() and pt.status>0 and pt.status<4";
            } else {
                return " and p.finish_time<now() and pt.status>0 and pt.status<4";
            }
        }
    }

    // 收尾阶段 456 offer 入职 保证期
    public String shouWei(Integer status) {
        return status != null ? " and pt.status=" + status : " and pt.status>3 and pt.status<7";
    }

    // 项目结束，通过保证期或淘汰
    public String jieShu(Integer status) {
        return status != null ? " and pt.status=" + status : " and pt.status>6";
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


    public void appendSort(Pageable pageable) {}
}
