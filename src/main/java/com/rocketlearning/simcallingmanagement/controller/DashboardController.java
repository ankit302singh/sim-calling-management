package com.rocketlearning.simcallingmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import com.rocketlearning.simcallingmanagement.service.UserService;

import com.rocketlearning.simcallingmanagement.service.SimDataService;


@Controller
public class DashboardController {
	@Autowired
	private SimDataService simDataService;
	
	@Autowired
	private UserService userService;

	@GetMapping("/dashboard")
	public String dashboard(Model model) {

	    model.addAttribute("totalSims",
	            simDataService.getTotalSims());

	    model.addAttribute("calledCount",
	            simDataService.getCalledCount());

	    model.addAttribute("inactiveCount",
	            simDataService.getInactiveCount());

	    model.addAttribute("noSimCount",
	            simDataService.getNoSimCount());

	    model.addAttribute("noNetworkCount",
	            simDataService.getNoNetworkCount());

	    model.addAttribute("callFailedCount",
	            simDataService.getCallFailedCount());

	    model.addAttribute("otherCount",
	            simDataService.getOtherCount());
	    
	    model.addAttribute("employeeCount",
	            userService.getTotalEmployees());

	    return "dashboard";
	}

}
