package com.moving.admin.dao.natives;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TaskNative extends AbstractNative {

    /////// 定时任务
    //// 专属人才转普通人才判定
    // 不在项目进展中的专属人才、30天内没有跟踪，置为普通人才
    public void talentTaskOne() {
        String update = "update talent t set t.type=0, t.folow_user_id=null";
        String set = " from talent t";
        String where = " where t.type=1 and (select count(1))";
        Session session = entityManager.unwrap(Session.class);
        NativeQuery query = session.createNativeQuery(update + set + where);
        query.executeUpdate();
    }

    public void appendSort(Pageable pageable) {}

}
