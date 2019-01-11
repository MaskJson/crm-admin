package com.moving.admin.dao.common;

import com.moving.admin.entity.common.Aptness;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AptnessDao extends JpaRepository<Aptness, Long>, JpaSpecificationExecutor<Aptness> {
}
