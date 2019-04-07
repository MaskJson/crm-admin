package com.moving.admin.service;

import com.moving.admin.dao.project.ProjectDao;
import com.moving.admin.entity.project.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ProjectService extends AbstractService {

    @Autowired
    private ProjectDao projectDao;

    // 编辑项目
    public Long save(Project project) {
        if (project.getId() == null) {
            project.setCreateTime(new Date(System.currentTimeMillis()));
        }
        project.setUpdateTime(new Date(System.currentTimeMillis()));
        projectDao.save(project);
        return project.getId();
    }

    // 获取项目详情
    public Project getById(Long id) {
        return projectDao.findById(id).get();
    }

    // 关注装修改
    public void toggleFollow(Long id, Boolean follow) {
        Project project = projectDao.findById(id).get();
        if (project != null) {
            project.setFollow(follow);
            save(project);
        }
    }

}
