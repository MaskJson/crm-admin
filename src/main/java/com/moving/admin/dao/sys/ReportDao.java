package com.moving.admin.dao.sys;

import com.moving.admin.entity.sys.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ReportDao  extends JpaRepository<Report, Long>, JpaSpecificationExecutor<Report> {
}
