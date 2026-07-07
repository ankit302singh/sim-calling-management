package com.rocketlearning.simcallingmanagement.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rocketlearning.simcallingmanagement.entity.SimData;
import com.rocketlearning.simcallingmanagement.entity.SimStatus;
import com.rocketlearning.simcallingmanagement.repository.SimDataRepository;

@Service
public class SimDataService {

    @Autowired
    private SimDataRepository simDataRepository;
    
    @Autowired
    private AssignmentHistoryService assignmentHistoryService;
    
    @Autowired
    private ActivityLogService activityLogService;
    

    public List<SimData> getAllSims() {

        return simDataRepository.findAll();

    }

    public void saveSim(SimData simData) {

        System.out.println("===== saveSim() CALLED =====");

        SimData existingSim =
                simDataRepository.findBySimNumber(simData.getSimNumber());

        // New SIM
        if (existingSim == null) {

            simDataRepository.save(simData);
            
            activityLogService.saveLog(
                    getLoggedInUser(),
                    "SIM",
                    "Add",
                    "Added SIM : " + simData.getSimNumber());

            if (simData.getAssignedEmployee() != null
                    && !simData.getAssignedEmployee().isBlank()) {

                assignmentHistoryService.saveAssignmentHistory(
                        simData.getOrg(),
                        simData.getSimNumber(),
                        simData.getPhoneLabel(),
                        simData.getAssignedEmployee(),
                        "",                       // Employee Email (we'll improve later)
                        "Initial Assignment",
                        simData.getRemarks(),
                        "getLoggedInUser()");

            }

            return;
        }

        // Existing SIM
        if (!Objects.equals(existingSim.getAssignedEmployee(),
                simData.getAssignedEmployee())) {

            assignmentHistoryService.closePreviousAssignment(
                    simData.getSimNumber());

            assignmentHistoryService.saveAssignmentHistory(
                    simData.getOrg(),
                    simData.getSimNumber(),
                    simData.getPhoneLabel(),
                    simData.getAssignedEmployee(),
                    "",
                    "Reassigned",
                    simData.getRemarks(),
                    "getLoggedInUser()");
        }

        simDataRepository.save(simData);
        
        System.out.println("===== EDIT LOG CALLED =====");

        activityLogService.saveLog(
                getLoggedInUser(),
                "SIM",
                "Edit",
                "Updated SIM : " + simData.getSimNumber());
        
        activityLogService.saveLog(
                getLoggedInUser(),
                "SIM",
                "Edit",
                "Updated SIM : " + simData.getSimNumber());

    }
    
    public long getTotalSims() {

        return simDataRepository.count();

    }

    public long getCalledCount() {

        return simDataRepository.countByStatus(SimStatus.CALLED);

    }

    public long getInactiveCount() {

        return simDataRepository.countByStatus(SimStatus.INACTIVE);

    }

    public long getNoSimCount() {

        return simDataRepository.countByStatus(SimStatus.NO_SIM);

    }

    public long getNoNetworkCount() {

        return simDataRepository.countByStatus(SimStatus.NO_NETWORK);

    }

    public long getCallFailedCount() {

        return simDataRepository.countByStatus(SimStatus.CALL_FAILED);

    }

    public long getOtherCount() {

        return simDataRepository.countByStatus(SimStatus.OTHER);

    }
    public SimData getSimById(Long id) {

        return simDataRepository.findById(id).orElse(null);

    }
    
    public List<SimData> getSimsByEmployee(String employeeName) {

        return simDataRepository.findByAssignedEmployee(employeeName);

    }
    
    public List<SimData> searchSimData(String keyword) {

        if (keyword == null || keyword.isBlank()) {

            return simDataRepository.findAll();

        }

        return simDataRepository.searchSimData(keyword.trim());

    }
    
    public List<SimData> filterSimData(
            String keyword,
            SimStatus status,
            String employee,
            String organization) {

        if (keyword != null && keyword.isBlank()) {
            keyword = null;
        }

        if (employee != null && employee.isBlank()) {
            employee = null;
        }

        if (organization != null && organization.isBlank()) {
            organization = null;
        }

        return simDataRepository.filterSimData(
                keyword,
                status,
                employee,
                organization);

    }
    
    public Page<SimData> filterSimData(
            String keyword,
            SimStatus status,
            String employee,
            String organization,
            Pageable pageable) {

        if (keyword != null && keyword.isBlank()) {
            keyword = null;
        }

        if (employee != null && employee.isBlank()) {
            employee = null;
        }

        if (organization != null && organization.isBlank()) {
            organization = null;
        }

        return simDataRepository.filterSimData(
                keyword,
                status,
                employee,
                organization,
                pageable);
    }
    public Page<SimData> getAllSims(Pageable pageable) {

        return simDataRepository.findAll(pageable);

    }
    
    public List<SimData> getSelectedSims(List<Long> ids) {

        return simDataRepository.findAllById(ids);

    }
    
    @Transactional
    public void bulkAssign(List<Long> ids, String employee) {

        List<SimData> sims =
                simDataRepository.findAllById(ids);

        for (SimData sim : sims) {

            // Skip if already assigned to same employee
            if (employee.equals(sim.getAssignedEmployee())) {
                continue;
            }

            // Close previous assignment
            assignmentHistoryService.closePreviousAssignment(
                    sim.getSimNumber());

            // Update SIM
            sim.setAssignedEmployee(employee);

            // Create new assignment history
            assignmentHistoryService.saveAssignmentHistory(
                    sim.getOrg(),
                    sim.getSimNumber(),
                    sim.getPhoneLabel(),
                    employee,
                    "",
                    "Bulk Assignment",
                    sim.getRemarks(),
                    getLoggedInUser());

        }

        simDataRepository.saveAll(sims);

    }
    
    @Transactional
    public void bulkReassign(List<Long> ids,
                             String employee,
                             String reason) {

        List<SimData> sims =
                simDataRepository.findAllById(ids);

        for (SimData sim : sims) {

            // Skip if already assigned
            if (employee.equals(sim.getAssignedEmployee())) {
                continue;
            }

            // Close previous assignment
            assignmentHistoryService.closePreviousAssignment(
                    sim.getSimNumber());

            // Update employee
            sim.setAssignedEmployee(employee);

            // Save history with the user-entered reason
            assignmentHistoryService.saveAssignmentHistory(
                    sim.getOrg(),
                    sim.getSimNumber(),
                    sim.getPhoneLabel(),
                    employee,
                    "",
                    reason,
                    sim.getRemarks(),
                    getLoggedInUser());

        }

        simDataRepository.saveAll(sims);

    }
    
    public void deleteSelected(List<Long> ids) {

        simDataRepository.deleteAllById(ids);

    }
    
    private String getLoggedInUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        return authentication.getName();

    }
}