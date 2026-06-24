package com.rocketlearning.simcallingmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rocketlearning.simcallingmanagement.entity.SimData;

public interface SimDataRepository
        extends JpaRepository<SimData, Long> {

}