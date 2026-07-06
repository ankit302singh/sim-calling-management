package com.rocketlearning.simcallingmanagement.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rocketlearning.simcallingmanagement.entity.ActivityLog;
import com.rocketlearning.simcallingmanagement.repository.ActivityLogRepository;

@Service
public class ActivityLogService {

    @Autowired
    private ActivityLogRepository activityLogRepository;

    public void saveLog(
            String username,
            String module,
            String action,
            String description) {

        ActivityLog log = new ActivityLog();

        log.setActivityTime(LocalDateTime.now());

        log.setUsername(username);

        log.setModule(module);

        log.setAction(action);

        log.setDescription(description);

        activityLogRepository.save(log);

    }

}
