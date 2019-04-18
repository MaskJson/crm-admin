package com.moving.admin.service;

import com.moving.admin.dao.natives.TaskNative;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TaskService {

    @Autowired
    private TaskNative taskNative;

    // 每天凌晨执行定时任务
//    @Scheduled(cron = "0 59 23 * * ?")
    @Scheduled(cron = "*/5 * * * * ?")
    public void checkTalentType() {

    }

}
