package com.moving.admin.dao.project;

import com.moving.admin.entity.project.ProjectReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProjectReportDao extends JpaRepository<ProjectReport, Long>, JpaSpecificationExecutor<ProjectReport> {
}
