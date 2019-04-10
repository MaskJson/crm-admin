package com.moving.admin.dao.project;

import com.moving.admin.entity.project.ProjectTalent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectTalentDao extends JpaRepository<ProjectTalent, Long>, JpaSpecificationExecutor<ProjectTalent> {

    // 获取该项目下的所有人才
    List<ProjectTalent> findAllByProjectId(Long projectId);

    // 通过人才id获取关联的项目数，不包括已淘汰的
    @Query("select count(id) from ProjectTalent where talentId=:talentId and status<7")
    Long getProjectLengthByTalentId(@Param("talentId") Long talentId);

    // 查找某项目里是否已存在某人才，若有，重置状态
    ProjectTalent findProjectTalentByProjectIdAndTalentId(Long projectId, Long talentId);

    // 获取某项目下，各个状态的人才数量
    @Query("select count(id) from ProjectTalent where projectId=:projectId and status=:status")
    Long getCountByProjectIdAndStatus(@Param("projectId") Long projectId, @Param("status") Integer status);

    // 获取某项目下所有人才数量
    @Query("select count(id) from ProjectTalent where projectId=:projectId")
    Long getCountByProjectId(@Param("projectId") Long projectId);

    // 查看人才是否被其他系统用户列为项目进展人才
    List<ProjectTalent> findAllByTalentIdAndCreateUserIdNotAndStatusNotIn(Long talentId, Long createUserId, List<Integer> status);

}
