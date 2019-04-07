package com.moving.admin.dao.project;

import com.moving.admin.entity.project.ProjectTalent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ProjectTalentDao extends JpaRepository<ProjectTalent, Long>, JpaSpecificationExecutor<ProjectTalent> {

    // 获取该项目下的所有人才
    List<ProjectTalent> findAllByProjectId(Long projectId);

}
