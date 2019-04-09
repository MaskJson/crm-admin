package com.moving.admin.dao.talent;

import com.moving.admin.entity.talent.TalentRemind;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TalentRemindDao extends JpaRepository<TalentRemind, Long>, JpaSpecificationExecutor<TalentRemind> {

    List<TalentRemind> findAllByTalentIdOrderByIdDesc(Long talentId);

    List<TalentRemind> findAllByTalentIdAndCreateUserIdOrderByIdDesc(Long talentId, Long createUserId);

}
