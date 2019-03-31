package com.moving.admin.dao.talent;

import com.moving.admin.entity.talent.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ExperienceDao extends JpaRepository<Experience, Long>, JpaSpecificationExecutor<Experience> {
}
