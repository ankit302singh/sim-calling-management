package com.rocketlearning.simcallingmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rocketlearning.simcallingmanagement.entity.SimData;
import com.rocketlearning.simcallingmanagement.entity.SimStatus;

public interface SimDataRepository
        extends JpaRepository<SimData, Long> {

    long countByStatus(SimStatus status);

    SimData findBySimNumber(String simNumber);

    List<SimData> findByAssignedEmployee(String assignedEmployee);

    @Query("""
    SELECT s
    FROM SimData s
    WHERE

    LOWER(s.phoneLabel) LIKE LOWER(CONCAT('%', :keyword, '%'))

    OR LOWER(s.simNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))

    OR LOWER(s.mobileNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))

    OR LOWER(s.organization) LIKE LOWER(CONCAT('%', :keyword, '%'))

    OR LOWER(s.assignedEmployee) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    List<SimData> searchSimData(
            @Param("keyword") String keyword);

}