package com.rocketlearning.simcallingmanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.rocketlearning.simcallingmanagement.entity.SimData;
import com.rocketlearning.simcallingmanagement.service.SimDataService;

@Controller
public class SimDataController {

    @Autowired
    private SimDataService simDataService;

    @GetMapping("/sims")
    public String sims(Model model) {

        List<SimData> sims = simDataService.getAllSims();

        model.addAttribute("sims", sims);

        return "sims";
    }
    @GetMapping("/sims/add")
    public String addSimPage() {

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

        return "add-sim";

    }

}