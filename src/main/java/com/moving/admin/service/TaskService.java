package com.moving.admin.service;

import com.moving.admin.dao.natives.TaskNative;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TaskService {

    @Autowired
    private TaskNative taskNative;

//    @Scheduled(cron = "0 43 14 * * ?")
    @Scheduled(cron = "*/5 * * * * ?")
    public void checkTalentType() {

    }

}
