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

    		OR LOWER(s.org) LIKE LOWER(CONCAT('%', :keyword, '%'))

    		OR LOWER(s.assignedEmployee) LIKE LOWER(CONCAT('%', :keyword, '%'))

    		OR LOWER(s.remarks) LIKE LOWER(CONCAT('%', :keyword, '%'))
    		""")
    List<SimData> searchSimData(
            @Param("keyword") String keyword);
    
    @Query("""
    		SELECT s
    		FROM SimData s
    		WHERE

    		(:keyword IS NULL
    		OR LOWER(s.phoneLabel) LIKE LOWER(CONCAT('%', :keyword, '%'))
    		OR LOWER(s.simNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))
    		OR LOWER(s.mobileNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))
    		OR LOWER(s.org) LIKE LOWER(CONCAT('%', :keyword, '%'))
    		OR LOWER(s.assignedEmployee) LIKE LOWER(CONCAT('%', :keyword, '%'))
    		OR LOWER(s.remarks) LIKE LOWER(CONCAT('%', :keyword, '%')))

    		AND

    		(:status IS NULL
    		OR s.status = :status)

    		AND

    		(:employee IS NULL
    		OR s.assignedEmployee = :employee)

    		AND

    		(:organization IS NULL
    		OR s.org = :organization)

    		""")
    		List<SimData> filterSimData(

    		        @Param("keyword") String keyword,

    		        @Param("status") SimStatus status,

    		        @Param("employee") String employee,

    		        @Param("organization") String organization

    		);

}