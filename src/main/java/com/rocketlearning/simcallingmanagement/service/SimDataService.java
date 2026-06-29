package com.rocketlearning.simcallingmanagement.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rocketlearning.simcallingmanagement.entity.SimData;
import com.rocketlearning.simcallingmanagement.repository.SimDataRepository;
import com.rocketlearning.simcallingmanagement.entity.SimStatus;

@Service
public class SimDataService {

    @Autowired
    private SimDataRepository simDataRepository;
    
    @Autowired
    private AssignmentHistoryService assignmentHistoryService;

    public List<SimData> getAllSims() {

        return simDataRepository.findAll();

    }

    public void saveSim(SimData simData) {

        SimData existingSim =
                simDataRepository.findBySimNumber(simData.getSimNumber());

        // New SIM
        if (existingSim == null) {

            simDataRepository.save(simData);

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
                        "Admin");

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
                    "Admin");
        }

        simDataRepository.save(simData);

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

}