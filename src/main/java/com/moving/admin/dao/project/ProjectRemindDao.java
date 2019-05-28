package com.moving.admin.dao.project;

import com.moving.admin.entity.project.ProjectRemind;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ProjectRemindDao extends JpaRepository<ProjectRemind, Long>, JpaSpecificationExecutor<ProjectRemind> {

    List<ProjectRemind> findAllByCreateUserId(Long createUserId);

    List<ProjectRemind> findAllByProjectTalentIdOrderByCreateTimeDesc(Long projectTalentId);

    List<ProjectRemind> findAllByProjectTalentIdAndCreateUserIdOrderByCreateTimeDesc(Long projectTalentId, Long createUserId);

    List<ProjectRemind> findAllById(Long id);

    // 根据projectTalentId、status、type获取reminds
    List<ProjectRemind> findAllByProjectTalentIdAndStatusAndType(Long projectTalentId, Integer status, Integer type);
}
