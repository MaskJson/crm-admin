package com.moving.admin.dao.talent;

import com.moving.admin.entity.talent.TalentRemind;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TalentRemindDao extends JpaRepository<TalentRemind, Long>, JpaSpecificationExecutor<TalentRemind> {
}
