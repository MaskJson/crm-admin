package com.moving.admin.dao.project;

import com.moving.admin.entity.project.ProjectAdviser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectAdviserDao extends JpaRepository<ProjectAdviser, Long>, JpaSpecificationExecutor<ProjectAdviser> {

    void deleteAllByProjectId(Long projectId);

    @Query("select userId from ProjectAdviser where projectId=:projectId")
    List<Long> findAllByProjectId(@Param("projectId") Long projectId);

}
