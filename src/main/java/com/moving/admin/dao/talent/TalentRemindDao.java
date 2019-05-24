package com.moving.admin.dao.talent;

import com.moving.admin.entity.talent.TalentRemind;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface TalentRemindDao extends JpaRepository<TalentRemind, Long>, JpaSpecificationExecutor<TalentRemind> {

    List<TalentRemind> findAllByTalentIdOrderByIdDesc(Long talentId);

    List<TalentRemind> findAllByTalentIdAndCreateUserIdOrderByIdDesc(Long talentId, Long createUserId);

    // 获取该人才在某一时间后的一个月内的跟踪
    @Query(value = "select count (id) from TalentRemind where talentId=:talentId and createTime>:start and createTime<:end")
    Long getTaskCountByTalentId(@Param("talentId") Long talentId, @Param("start") Date start, @Param("end") Date end);

    List<TalentRemind> findAllByCreateUserId(Long createUserId);

    // 获取最后一次未跟进的跟踪
    List<TalentRemind> findByTalentIdAndFinishAndCreateUserIdOrderByCreateTimeDesc(Long talentId, Boolean finish, Long createUserId);

}
