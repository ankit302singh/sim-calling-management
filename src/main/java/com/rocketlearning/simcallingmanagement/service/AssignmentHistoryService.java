package com.rocketlearning.simcallingmanagement.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rocketlearning.simcallingmanagement.entity.AssignmentHistory;
import com.rocketlearning.simcallingmanagement.repository.AssignmentHistoryRepository;

@Service
public class AssignmentHistoryService {

    @Autowired
    private AssignmentHistoryRepository assignmentHistoryRepository;
    
    public void saveAssignmentHistory(String org,
            String simNumber,
            String phoneLabel,
            String employeeName,
            String employeeEmail,
            String reason,
            String remarks,
            String updatedBy) {

AssignmentHistory history = new AssignmentHistory();

history.setOrg(org);
history.setSimNumber(simNumber);
history.setPhoneLabel(phoneLabel);
history.setEmployeeName(employeeName);
history.setEmployeeEmail(employeeEmail);

history.setAssignedDate(LocalDate.now());

history.setRemovedDate(null);

history.setReason(reason);

history.setRemarks(remarks);

history.setUpdatedBy(updatedBy);

assignmentHistoryRepository.save(history);

}
    public void closePreviousAssignment(String simNumber) {

        AssignmentHistory history =
                assignmentHistoryRepository
                .findBySimNumberAndRemovedDateIsNull(simNumber);

        if (history != null) {

            history.setRemovedDate(LocalDate.now());

            assignmentHistoryRepository.save(history);

        }

    }

}
