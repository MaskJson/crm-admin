package com.moving.admin.dao.natives;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskNative extends AbstractNative {

    /////// 定时任务
    //// 专属人才转普通人才判定
    // 不在项目进展中的  专属人才、30天内没有跟踪，置为普通人才

    private String update = "update talent t set t.type=0, t.follow_user_id=null";

    public void talentTaskOne() {
        String where = " where t.type=1 and " + progressWhere() + "<1 and " + remindWhere() + "<1 and " + successWhere() + "<1";
        Session session = entityManager.unwrap(Session.class);
        NativeQuery query = session.createNativeQuery(update + where);
        query.executeUpdate();
    }

    //// 专属人才转普通人才判定
    // 离开项目进展，且当前不在进展、没有通过保证期一年内的人才，离开后三十天内没跟踪的，置为普通人才
    public void talentTaskSecond() {
        String where = " t.type=1 and " + progressWhere() + "<1 and " + successWhere() + "<1 and" + leaveWhere() + "<1";
        Session session = entityManager.unwrap(Session.class);
        NativeQuery query = session.createNativeQuery(update + where);
        query.executeUpdate();
    }

    //// 专属人才转普通人才判定
    // 通过保证期后一年 且没有三十天内跟踪过的记录，职位普通人才
    public void talentTaskThird() {
        String where = " t.type = 1";
        Session session = entityManager.unwrap(Session.class);
        NativeQuery query = session.createNativeQuery(update + where);
    }

    // 获取淘汰掉的且淘汰后三十天内   有过  跟踪的记录， 若存在淘汰后三十天内跟踪过的记录，
    public String leaveWhere() {
        return " (select count(1) from project_talent pt where pt.status=8 and " +
                "(select count(1) from remind r where r.talent_id=pt.talent_id and date_add(pt.update_time, interval 30 day) > r.create_time)" +
                ">0)";
    }

    // 获取三十天以内的跟踪数量
    public String remindWhere() {
        return " (select count(1) from remind r where r.talent_id=t.id and date_add(r.create_time, interval 30 day) > now())";
    }

    // 获取人才在项目进展中的数量
    public String progressWhere() {
        return " (select count(1) from project_talent pt where pt.talent_id=t.id and pt.status<8)";
    }

    // 获取人才通过保证期不超过一年的项目数量
    public String successWhere() {
        return " (select count(1) from project_talent pt where pt.status=7 and date_add(pt.update_time, interval 365 day) > now())";
    }

    public void appendSort(Pageable pageable) {}

}
