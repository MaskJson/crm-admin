package com.moving.admin.dao.natives;

import com.moving.admin.dao.project.ProjectTalentDao;
import com.moving.admin.dao.talent.TalentDao;
import com.moving.admin.dao.talent.TalentRemindDao;
import com.moving.admin.entity.project.ProjectTalent;
import com.moving.admin.entity.talent.Talent;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class TaskNative extends AbstractNative {

    @Autowired
    private ProjectTalentDao projectTalentDao;

    @Autowired
    private TalentDao talentDao;

    @Autowired
    private TalentRemindDao talentRemindDao;

    /////// 定时任务
    //// 专属人才转普通人才判定

    private String update = "update talent t set t.type=0, t.follow_user_id=null where";

    // 不在项目进展中的  专属人才、30天内没有跟踪，置为普通人才
    @Transactional
    public void talentTaskWithoutProject() {
        //  当前没处在项目进展、没有结束状态的项目进展，三十天内未联系则降级
        String where = " t.type=1 and " +  projectWhere("t.id") + "<1 and " + remindWhere() + "<1";
        Session session = entityManager.unwrap(Session.class);
        NativeQuery query = session.createNativeQuery(update + where);
        query.executeUpdate();
    }

    // 获取人才最后一条进展结束的记录，若是淘汰，淘汰后一个月没跟踪联系的降级，通过保证期的一年后的一个月中没跟踪记录的降级 (  且近一个月内没联系 )
    @Transactional
    public void talentTaskWithFinish() {
        List<Talent> talents = talentDao.findAllByType(1);
        talents.forEach(talent -> {
            List<ProjectTalent> list = projectTalentDao.findAllByTalentIdAndStatusIsAfterOrderByUpdateTime(talent.getId(), 6);
            if (list.size() > 0) {
                // 获取最后一条离开进展的记录
                ProjectTalent pt = list.get(0);
                Long ptTimes = pt.getUpdateTime().getTime();
                Long nowTimes = new Date().getTime();
                BigInteger progress = getTotal(projectWhere(talent.getId().toString()));
                BigInteger monthRemind = getTotal(remindWhere());
                if (pt.getStatus() == 7 && (ptTimes + 2592000000L) <= nowTimes) {
                    // 若是淘汰，淘汰后一个月没跟踪联系的降级
                    // 获取从淘汰时间起的跟踪记录
                    Long size = talentRemindDao.getTaskCountByTalentId(talent.getId(), pt.getUpdateTime(), new Date(ptTimes + 2592000000L));
                    if (size == 0 && progress == objectToBigInteger(0) && monthRemind == objectToBigInteger(0)) {
                        talent.setType(0);
                        talent.setFollowUserId(null);
                        talentDao.save(talent);
                    }
                } else if ((ptTimes + 2592000000L * 13) <= nowTimes){
                    // 通过保证期的一年后的一个月中没跟踪记录的降级
                    Long size = talentRemindDao.getTaskCountByTalentId(talent.getId(), new Date(ptTimes + 2592000000L * 12), new Date(ptTimes + 2592000000L * 13));
                    if (size == 0 && progress == objectToBigInteger(0) && monthRemind == objectToBigInteger(0)) {
                        talent.setType(0);
                        talent.setFollowUserId(null);
                        talentDao.save(talent);
                    }
                }
            }
        });
    }

    // 进入保证期且已到通过保证期时间的，改变进展人才的状态为成功状态
    @Transactional
    public void projectTalentTask() {
        String sql = "update project_talent set status=7 where status=6 and now() > probation_time";
        Session session = entityManager.unwrap(Session.class);
        NativeQuery query = session.createNativeQuery(sql);
        query.executeUpdate();
    }

    // 获取该人才是在项目进展的数量
    public String projectWhere(String id) {
        return " (select count(1) from project_talent pt where pt.talent_id=" + id + ")";
    }

    // 获取三十天以内的跟踪数量
    public String remindWhere() {
        return " (select count(1) from talent_remind r where r.talent_id=t.id and date_add(r.create_time, interval 30 day) > now())";
    }

    // 获取总数
    public BigInteger getTotal(String sql) {
        Query query = entityManager.createNativeQuery(sql);
        return objectToBigInteger(query.getSingleResult());
    }

    public void appendSort(Pageable pageable) {}

}
