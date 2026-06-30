package com.rocketlearning.simcallingmanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.rocketlearning.simcallingmanagement.security.CustomUserDetails;

import com.rocketlearning.simcallingmanagement.entity.Role;
import com.rocketlearning.simcallingmanagement.entity.SimData;
import com.rocketlearning.simcallingmanagement.entity.User;
import com.rocketlearning.simcallingmanagement.service.SimDataService;
import com.rocketlearning.simcallingmanagement.service.UserService;

@Controller
public class EmployeeController {

    @Autowired
    private UserService userService;

    @Autowired
    private SimDataService simDataService;

    // ================= ADMIN =================

    @GetMapping("/employees")
    public String employees(Model model) {

        List<User> employees = userService.getAllEmployees();

        model.addAttribute("employees", employees);

        return "employees";
    }

    @GetMapping("/employees/add")
    public String addEmployeePage() {

        return "add-employee";

    }

    @PostMapping("/employees/save")
    public String saveEmployee(@ModelAttribute User user) {

        user.setRole(Role.EMPLOYEE);

        userService.saveEmployee(user);

        return "redirect:/employees";

    }

    // ================= EMPLOYEE =================

    @GetMapping("/employee/dashboard")
    public String employeeDashboard(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {

        User currentUser = userDetails.getUser();

        List<SimData> mySims =
                simDataService.getSimsByEmployee(currentUser.getName());

        model.addAttribute("employee", currentUser);

        model.addAttribute("mySims", mySims);

        model.addAttribute("totalAssigned", mySims.size());

        return "employee-dashboard";

    }

    @GetMapping("/employee/my-sims")
    public String mySims(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {

        User currentUser = userDetails.getUser();

        List<SimData> mySims =
                simDataService.getSimsByEmployee(currentUser.getName());

        model.addAttribute("mySims", mySims);

        model.addAttribute("employee", currentUser);

        return "my-sims";

    }
    
    @GetMapping("/employee/update/{id}")
    public String employeeUpdatePage(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {

        User currentUser = userDetails.getUser();

        SimData simData = simDataService.getSimById(id);

        if (simData == null) {

            return "redirect:/employee/my-sims";

        }

        model.addAttribute("employee", currentUser);

        model.addAttribute("sim", simData);

        return "employee-update";

    }
    
    @PostMapping("/employee/update")
    public String updateEmployeeSim(@ModelAttribute SimData simData) {

        SimData existingSim = simDataService.getSimById(simData.getId());

        if (existingSim != null) {

            // Employee can update only these fields
            existingSim.setStatus(simData.getStatus());
            existingSim.setRemarks(simData.getRemarks());

            simDataService.saveSim(existingSim);

        }

        return "redirect:/employee/my-sims";

    }

}