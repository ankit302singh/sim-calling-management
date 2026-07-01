package com.rocketlearning.simcallingmanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rocketlearning.simcallingmanagement.entity.SimData;
import com.rocketlearning.simcallingmanagement.entity.SimStatus;
import com.rocketlearning.simcallingmanagement.service.SimDataService;
import com.rocketlearning.simcallingmanagement.service.UserService;

@Controller
public class SimDataController {

    @Autowired
    private SimDataService simDataService;
    
    @Autowired
    private UserService userService;

    @GetMapping("/sims")
    public String sims(

            @RequestParam(required = false) String keyword,

            @RequestParam(required = false) SimStatus status,

            @RequestParam(required = false) String employee,

            @RequestParam(required = false) String organization,

            Model model) {

        List<SimData> sims =
                simDataService.filterSimData(
                        keyword,
                        status,
                        employee,
                        organization);

        model.addAttribute("sims", sims);

        model.addAttribute("keyword", keyword);

        model.addAttribute("status", status);

        model.addAttribute("employee", employee);

        model.addAttribute("organization", organization);

        return "sims";

    }
    @GetMapping("/sims/add")
    public String addSimPage(Model model) {

        model.addAttribute("simData", new SimData());

        model.addAttribute("employees",
                userService.getAllEmployees());

        return "add-sim";

    }
    @PostMapping("/sims/save")
    public String saveSim(@ModelAttribute SimData simData) {

        simDataService.saveSim(simData);

        return "redirect:/sims";

    }
    
    @GetMapping("/sims/edit/{id}")
    public String editSim(@PathVariable Long id, Model model) {

        SimData simData = simDataService.getSimById(id);

        model.addAttribute("simData", simData);

        model.addAttribute("employees",
                userService.getAllEmployees());

        return "add-sim";

    }

}