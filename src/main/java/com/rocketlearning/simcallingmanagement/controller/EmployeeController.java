package com.rocketlearning.simcallingmanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.rocketlearning.simcallingmanagement.entity.Role;

import com.rocketlearning.simcallingmanagement.entity.User;
import com.rocketlearning.simcallingmanagement.service.UserService;

@Controller
public class EmployeeController {

    @Autowired
    private UserService userService;

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
}