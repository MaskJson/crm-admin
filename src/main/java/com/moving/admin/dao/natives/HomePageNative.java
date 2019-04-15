package com.moving.admin.dao.natives;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import java.math.BigInteger;
import java.util.HashMap;
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

    // 统计项目进展，四大阶段的相关人才数量
    public Map<String, BigInteger> homeCountProjectTalent(Map<String, BigInteger> map, Long userId, Long roleId) {
        String countSelect = "select count(1)";
//        System.err.println(countSelect + getCountProjectFrom() + getTeamMemberSql(roleId, userId) + qiDong(null));
//        map.put("qiDong", getTotal(countSelect + getCountProjectFrom() + getTeamMemberSql(roleId, userId) + qiDong(null)));
//        map.put("gongJian", getTotal(countSelect + getCountProjectFrom() + getTeamMemberSql(roleId, userId) + gongJian(null)));
//        map.put("shouWei", getTotal(countSelect + getCountProjectFrom() + getTeamMemberSql(roleId, userId) + shouWei(null)));
//        map.put("jieShu", getTotal(countSelect + getCountProjectFrom() + getTeamMemberSql(roleId, userId) + jieShu(null)));
        return map;
    }

    public String getCountProjectFrom() {
        return " from project_talent pt left join project p on pt.project_id=p.id left join talent t on t.id=pt.talent_id";
    }

    public String getTeamMemberSql(Long roleId, Long userId) {
        String whereStart = " where p.team_id in (select " + (roleId == 3 ? "id" : "team_id") +" from team where user_id=" + userId + ") and" +
                " pt.create_user_id in(select user_id from team where level " + (roleId == 3 ? "in(2,3,4,5)" : "is null");
        return whereStart;
    }

    //////// 获取查询条件
    //// 启动阶段
    public String qiDong(Integer status) {
        return " and pt.status=0";
    }

    // 攻坚阶段，推荐成功，且未淘汰，未超过计划完成时间
    public String gongJian(Integer status) {
        return status != null ? " and pt.status=" + status : " and pt.status>0 and pt.status<4";
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
