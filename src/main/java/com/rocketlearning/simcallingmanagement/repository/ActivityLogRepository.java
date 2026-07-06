package com.rocketlearning.simcallingmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rocketlearning.simcallingmanagement.entity.ActivityLog;

public interface ActivityLogRepository
        extends JpaRepository<ActivityLog, Long> {

}
