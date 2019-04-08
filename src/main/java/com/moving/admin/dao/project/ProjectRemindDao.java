package com.moving.admin.dao.project;

import com.moving.admin.entity.project.ProjectRemind;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProjectRemindDao extends JpaRepository<ProjectRemind, Long>, JpaSpecificationExecutor<ProjectRemind> {
}
