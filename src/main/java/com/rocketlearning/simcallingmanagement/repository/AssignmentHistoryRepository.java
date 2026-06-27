package com.rocketlearning.simcallingmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rocketlearning.simcallingmanagement.entity.AssignmentHistory;

public interface AssignmentHistoryRepository
        extends JpaRepository<AssignmentHistory, Long> {

    AssignmentHistory findBySimNumberAndRemovedDateIsNull(String simNumber);

}