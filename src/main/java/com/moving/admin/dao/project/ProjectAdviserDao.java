package com.moving.admin.dao.project;

import com.moving.admin.entity.project.ProjectAdviser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProjectAdviserDao extends JpaRepository<ProjectAdviser, Long>, JpaSpecificationExecutor<ProjectAdviser> {

    void deleteAllByProjectId(Long projectId);

}
