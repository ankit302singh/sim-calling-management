package com.rocketlearning.simcallingmanagement.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;
import com.rocketlearning.simcallingmanagement.entity.SimData;
import com.rocketlearning.simcallingmanagement.entity.SimStatus;
import com.rocketlearning.simcallingmanagement.service.ExcelExportService;
import com.rocketlearning.simcallingmanagement.service.ExcelImportService;
import com.rocketlearning.simcallingmanagement.service.SimDataService;
import com.rocketlearning.simcallingmanagement.service.UserService;

@Controller
public class SimDataController {

    @Autowired
    private SimDataService simDataService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ExcelImportService excelImportService;
    
    @Autowired
    private ExcelExportService excelExportService;

    @GetMapping("/sims")
    public String sims(

            @RequestParam(required = false) String keyword,

            @RequestParam(required = false) SimStatus status,

            @RequestParam(required = false) String employee,

            @RequestParam(required = false) String organization,

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "20") int size,

            Model model) {

        Pageable pageable = PageRequest.of(page, size);

        Page<SimData> simPage =
                simDataService.filterSimData(
                        keyword,
                        status,
                        employee,
                        organization,
                        pageable);

        model.addAttribute("simPage", simPage);

        model.addAttribute("sims", simPage.getContent());

        model.addAttribute("keyword", keyword);

        model.addAttribute("status", status);

        model.addAttribute("employee", employee);

        model.addAttribute("organization", organization);
        
        model.addAttribute(
                "employees",
                userService.getAllEmployees());

        model.addAttribute("currentPage", page);

        model.addAttribute("pageSize", size);
        
        model.addAttribute("totalPages", simPage.getTotalPages());
        
        long startRecord = (long) page * size + 1;

        long endRecord = Math.min(startRecord + size - 1,
                                  simPage.getTotalElements());

        if (simPage.getTotalElements() == 0) {

            startRecord = 0;
            endRecord = 0;

        }

        model.addAttribute("startRecord", startRecord);

        model.addAttribute("endRecord", endRecord);

        model.addAttribute("totalRecords",
                           simPage.getTotalElements());

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
    
    @PostMapping("/sims/import")
    public String importExcel(
            @RequestParam("file") MultipartFile file) {

        System.out.println();

        System.out.println("========== IMPORT REQUEST ==========");

        excelImportService.readExcel(file);

        System.out.println("Content Type : " + file.getContentType());

        System.out.println("====================================");

        System.out.println();

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
    
    @GetMapping("/sims/export")
    public ResponseEntity<byte[]> exportExcel() throws IOException {

        List<SimData> sims = simDataService.getAllSims();

        byte[] excel = excelExportService.exportToExcel(sims);

        HttpHeaders headers = new HttpHeaders();

        headers.add(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=sim_data.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excel);

    }
    
    @PostMapping("/sims/export-selected")
    public ResponseEntity<byte[]> exportSelected(
            @RequestParam String selectedIds) throws IOException {

        List<Long> ids =
                Arrays.stream(selectedIds.split(","))
                        .map(Long::parseLong)
                        .collect(Collectors.toList());

        List<SimData> sims =
                simDataService.getSelectedSims(ids);

        byte[] excel =
                excelExportService.exportToExcel(sims);

        HttpHeaders headers = new HttpHeaders();

        headers.add(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=selected_sim_data.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excel);

    }
    
    @PostMapping("/sims/bulk-assign")
    public String bulkAssign(
            @RequestParam String selectedIds,
            @RequestParam String employee,
            @RequestParam(required = false) String reason) {

        List<Long> ids =
                Arrays.stream(selectedIds.split(","))
                        .map(Long::parseLong)
                        .toList();

        simDataService.bulkAssign(ids, employee);

        return "redirect:/sims";
    }
    
    @PostMapping("/sims/bulk-reassign")
    public String bulkReassign(
            @RequestParam String selectedIds,
            @RequestParam String employee,
            @RequestParam String reason) {

        List<Long> ids = Arrays.stream(selectedIds.split(","))
                .map(Long::parseLong)
                .toList();

        simDataService.bulkReassign(ids, employee, reason);

        return "redirect:/sims";
    }

}