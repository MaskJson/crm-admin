package com.moving.admin.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TaskService {

    @Scheduled(cron = "0 43 14 * * ?")
    public void checkTalentType() {
        System.err.println("task");
    }

}
