package com.moving.admin.dao.performance;

import com.moving.admin.dao.natives.AbstractNative;
import com.moving.admin.dao.natives.CountNative;
import com.moving.admin.dao.talent.TalentRemindDao;
import com.moving.admin.entity.talent.TalentRemind;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PerformanceTalentRemind extends AbstractNative {

    @Autowired
    private CountNative countNative;

    @Autowired
    private TalentRemindDao talentRemindDao;

    private final String select = "select tr.id,tr.type,tr.remark,tr.situation,tr.cause,tr.salary,tr.meet_time as meetTime,tr.meet_address as meetAddress," +
            "t.id as talentId,t.name as talentName,u.nick_name as createUser,tr.create_time as createTime,tr.create_user_id as createUserId";
    private final String from = " from talent_remind tr left join talent t on t.id=tr.talent_id left join sys_user u on u.id=tr.create_user_id";
    private final String sort = " order by tr.create_user_id, tr.talent_id, tr.create_time desc ";
    private final String where = " where tr.create_user_id=";

    //人才常规跟踪 日、周、月绩效
    public List<Map<String, Object>> getPerformance(Long userId, Integer flag, String time) {
        String whereStr = "";
        if (StringUtils.isEmpty(time)) {
            time = "now()";
        } else {
            time = "'"+time+"'";
        }
        switch (flag) {
            case 1: whereStr = " and to_days(tr.create_time) = to_days("+time+")";break;
            case 2: whereStr = " and YEARWEEK(date_format(tr.create_time, '%Y-%m-%d')) = YEARWEEK("+time+", '%Y-%m-%d')";break;
            case 3: whereStr = " and DATE_FORMAT(tr.create_time, '%Y%m') = "+time;break;
        }
        return getTalentReminds(where + userId + whereStr);
    }
    //人才常规跟踪日、 周、月报表
    public List<Map<String, Object>> getPerformanceReport(Long userId, Long roleId, Integer flag, String time, Long memberId) {
        String where = "";
        if (StringUtils.isEmpty(time)) {
            time = "now()";
        } else {
            time = "'"+time+"'";
        }
        String levelFilter = flag == 1 ? "" : " level is null and ";
        if (memberId != null) {
            where = " where tr.create_user_id=" + memberId;
        } else {
            switch (Integer.parseInt(roleId.toString())) {
                case 2:
                case 6:
                case 7: where = " where tr.create_user_id in(select user_id from team where "+levelFilter+" parent_id in(select id from team where level in(2,3,4) and user_id="+userId+"))";break;
                case 3: where = " where tr.create_user_id in (select user_id from team where team_id in(select id from team where level=1 and user_id="+userId+"))";break;
                case 1: where = " where tr.create_user_id in (select user_id from team where level=1)";break;
            }
        }
        switch (flag) {
            case 1:where = where + " and to_days(tr.create_time) = to_days("+time+")";break;
            case 2:where = where + " and YEARWEEK(date_format(tr.create_time, '%Y-%m-%d')) = YEARWEEK("+time+", '%Y-%m-%d')";break;
            case 3:where = where + " and DATE_FORMAT(tr.create_time, '%Y%m') = "+time;break;
        }
        return getTalentReminds(where);
    }

    public List<Map<String, Object>> getTalentReminds(String where) {
        Session session = entityManager.unwrap(Session.class);
        NativeQuery<Map<String, Object>> query = session.createNativeQuery(select + from + where + sort);
        query.addScalar("id", StandardBasicTypes.LONG);
        query.addScalar("createUserId", StandardBasicTypes.LONG);
        query.addScalar("talentId", StandardBasicTypes.LONG);
        query.addScalar("talentName", StandardBasicTypes.STRING);
        query.addScalar("type", StandardBasicTypes.INTEGER);
        query.addScalar("remark", StandardBasicTypes.STRING);
        query.addScalar("situation", StandardBasicTypes.STRING);
        query.addScalar("cause", StandardBasicTypes.STRING);
        query.addScalar("salary", StandardBasicTypes.STRING);
        query.addScalar("meetTime", StandardBasicTypes.TIMESTAMP);
        query.addScalar("meetAddress", StandardBasicTypes.STRING);
        query.addScalar("createUser", StandardBasicTypes.STRING);
        query.addScalar("createTime", StandardBasicTypes.TIMESTAMP);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.getResultList();
        list.forEach(item -> {
            item.put("info", countNative.getWorkInfo(Long.parseLong(item.get("talentId").toString())));
            Long id = Long.parseLong(item.get("id").toString());
            List<TalentRemind> reminds = talentRemindDao.findAllByIdBeforeOrderByCreateTimeDesc(id);
            item.put("prev", reminds.size()>0?reminds.get(0):new HashMap<>());
        });
        return list;
    }

    public void appendSort(Pageable pageable) {}
}
