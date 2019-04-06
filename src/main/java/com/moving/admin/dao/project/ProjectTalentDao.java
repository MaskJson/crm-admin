package com.moving.admin.dao.project;

import com.moving.admin.entity.project.ProjectTalent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProjectTalentDao extends JpaRepository<ProjectTalent, Long>, JpaSpecificationExecutor<ProjectTalent> {
}
