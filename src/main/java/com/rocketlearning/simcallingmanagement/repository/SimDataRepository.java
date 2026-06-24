package com.rocketlearning.simcallingmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rocketlearning.simcallingmanagement.entity.SimData;
import com.rocketlearning.simcallingmanagement.entity.SimStatus;

public interface SimDataRepository
        extends JpaRepository<SimData, Long> {
	long countByStatus(SimStatus status);

}