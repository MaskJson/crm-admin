package com.moving.admin.dao.project;

import com.moving.admin.entity.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProjectDao extends JpaRepository<Project, Long>, JpaSpecificationExecutor<Project> {
}
