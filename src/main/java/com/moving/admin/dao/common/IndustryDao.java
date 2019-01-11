package com.moving.admin.dao.common;

import com.moving.admin.entity.common.Industry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IndustryDao extends JpaRepository<Industry, Long>, JpaSpecificationExecutor<Industry> {
}
