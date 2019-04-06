package com.moving.admin.service;

import com.moving.admin.dao.natives.ProjectNative;
import com.moving.admin.dao.project.ProjectDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService extends AbstractService {

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private ProjectNative projectNative;

}
