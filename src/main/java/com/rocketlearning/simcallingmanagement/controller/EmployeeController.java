package com.rocketlearning.simcallingmanagement.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
    public String employeeDashboard(HttpSession session,
                                    Model model) {

        User currentUser =
                (User) session.getAttribute("loggedInUser");

        if (currentUser == null) {

            return "redirect:/login";

        }

        List<SimData> mySims =
                simDataService.getSimsByEmployee(currentUser.getName());

        model.addAttribute("employee", currentUser);

        model.addAttribute("mySims", mySims);

        model.addAttribute("totalAssigned", mySims.size());

        return "employee-dashboard";

    }

    @GetMapping("/employee/my-sims")
    public String mySims(HttpSession session,
                         Model model) {

        User currentUser =
                (User) session.getAttribute("loggedInUser");

        if (currentUser == null) {

            return "redirect:/login";

        }

        List<SimData> mySims =
                simDataService.getSimsByEmployee(currentUser.getName());

        model.addAttribute("mySims", mySims);

        model.addAttribute("employee", currentUser);

        return "my-sims";

    }

}