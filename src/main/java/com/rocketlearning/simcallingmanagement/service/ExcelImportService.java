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

            System.out.println();
            System.out.println("========== EXCEL INFO ==========");
            System.out.println("Sheet Name : " + sheet.getSheetName());
            System.out.println("Total Rows : " + sheet.getPhysicalNumberOfRows());
            System.out.println("================================");
            System.out.println();

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

                    System.out.println(
                            "Skipping Row "
                                    + (row.getRowNum() + 1)
                                    + " : Required fields are missing.");

                    continue;
                }

                // ==========================
                // Duplicate in Excel
                // ==========================

                if (importedSimNumbers.contains(simNumber)) {

                    System.out.println(
                            "Skipping Row "
                                    + (row.getRowNum() + 1)
                                    + " : Duplicate SIM Number found in Excel -> "
                                    + simNumber);

                    continue;
                }

                importedSimNumbers.add(simNumber);

                // ==========================
                // Duplicate in Database
                // ==========================

                if (simDataRepository.findBySimNumber(simNumber) != null) {

                    System.out.println(
                            "Skipping Row "
                                    + (row.getRowNum() + 1)
                                    + " : SIM Number already exists in Database -> "
                                    + simNumber);

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

            System.out.println();

            System.out.println("Total Valid SIM Objects : " + simList.size());

            // Save all valid SIMs
            if (!simList.isEmpty()) {

                simDataRepository.saveAll(simList);

                System.out.println("=================================");
                System.out.println("Excel Import Completed Successfully.");
                System.out.println("Imported Records : " + simList.size());
                System.out.println("=================================");

            } else {

                System.out.println("No valid records found to import.");

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