package com.rocketlearning.simcallingmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.rocketlearning.simcallingmanagement.service.AssignmentHistoryService;

@Controller
public class AssignmentHistoryController {

    @Autowired
    private AssignmentHistoryService assignmentHistoryService;

    @GetMapping("/assignment-history")
    public String assignmentHistory(Model model) {

        model.addAttribute("historyList",
                assignmentHistoryService.getAllHistory());

        return "assignment-history";

    }

}