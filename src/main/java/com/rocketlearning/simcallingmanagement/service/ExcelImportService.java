package com.rocketlearning.simcallingmanagement.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rocketlearning.simcallingmanagement.entity.SimData;
import com.rocketlearning.simcallingmanagement.entity.SimStatus;
import com.rocketlearning.simcallingmanagement.repository.SimDataRepository;

@Service
public class ExcelImportService {

    @Autowired
    private SimDataRepository simDataRepository;

    public int readExcel(MultipartFile file) {

        try {

            Workbook workbook =
                    WorkbookFactory.create(file.getInputStream());

            Sheet sheet = workbook.getSheetAt(0);

            

            List<SimData> simList = new ArrayList<>();

            Set<String> importedSimNumbers = new HashSet<>();

            for (Row row : sheet) {

                // Skip Header Row
                if (row.getRowNum() == 0) {
                    continue;
                }

                String org = getCellValue(row.getCell(0));
                String phoneLabel = getCellValue(row.getCell(1));
                String simNumber = getCellValue(row.getCell(2));
                String mobileNumber = getCellValue(row.getCell(3));
                String status = getCellValue(row.getCell(4));
                String assignedEmployee = getCellValue(row.getCell(5));
                String remarks = getCellValue(row.getCell(6));

                // ==========================
                // Required Field Validation
                // ==========================

                if (org.isBlank()
                        || phoneLabel.isBlank()
                        || simNumber.isBlank()
                        || mobileNumber.isBlank()) {

                   

                    continue;
                }

                // ==========================
                // Duplicate in Excel
                // ==========================

                if (importedSimNumbers.contains(simNumber)) {          

                    continue;
                }

                importedSimNumbers.add(simNumber);

                // ==========================
                // Duplicate in Database
                // ==========================

                if (simDataRepository.findBySimNumber(simNumber) != null) {
                  
                    continue;
                }

                // ==========================
                // Create SimData Object
                // ==========================

                SimData sim = new SimData();

                sim.setOrg(org);
                sim.setPhoneLabel(phoneLabel);
                sim.setSimNumber(simNumber);
                sim.setMobileNumber(mobileNumber);
                sim.setAssignedEmployee(assignedEmployee);
                sim.setRemarks(remarks);

                if (!status.isBlank()) {

                    sim.setStatus(
                            SimStatus.valueOf(status.toUpperCase()));

                }

                simList.add(sim);

            }


            // Save all valid SIMs
            if (!simList.isEmpty()) {

                simDataRepository.saveAll(simList);

               
            } else {

            }

            workbook.close();
            
            return simList.size();

        } catch (IOException e) {

            e.printStackTrace();

            return 0;

        }

    }

    /**
     * Returns cell value safely.
     */
    private String getCellValue(Cell cell) {

        if (cell == null) {
            return "";
        }

        return cell.toString().trim();

    }

}