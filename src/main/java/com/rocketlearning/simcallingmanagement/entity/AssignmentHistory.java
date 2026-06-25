package com.rocketlearning.simcallingmanagement.entity;

import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "assignment_history")


public class AssignmentHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    private String simNumber;

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSimNumber() {
		return simNumber;
	}

	public void setSimNumber(String simNumber) {
		this.simNumber = simNumber;
	}

	public String getPhoneLabel() {
		return phoneLabel;
	}

	public void setPhoneLabel(String phoneLabel) {
		this.phoneLabel = phoneLabel;
	}

	public String getAssignedEmployee() {
		return assignedEmployee;
	}

	public void setAssignedEmployee(String assignedEmployee) {
		this.assignedEmployee = assignedEmployee;
	}

	public LocalDate getAssignedDate() {
		return assignedDate;
	}

	public void setAssignedDate(LocalDate assignedDate) {
		this.assignedDate = assignedDate;
	}

	public LocalDate getRemovedDate() {
		return removedDate;
	}

	public void setRemovedDate(LocalDate removedDate) {
		this.removedDate = removedDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	private String phoneLabel;

    private String assignedEmployee;

    private LocalDate assignedDate;

    private LocalDate removedDate;

    private String remarks;

}
