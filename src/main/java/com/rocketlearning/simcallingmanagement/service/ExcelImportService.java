package com.rocketlearning.simcallingmanagement.service;

import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ExcelImportService {

    public void readExcel(MultipartFile file) {

        try {

            Workbook workbook =
                    WorkbookFactory.create(file.getInputStream());

            Sheet sheet = workbook.getSheetAt(0);

            System.out.println();

            System.out.println("========== EXCEL INFO ==========");

            System.out.println("Sheet Name : " + sheet.getSheetName());

            System.out.println("Total Rows : " + sheet.getPhysicalNumberOfRows());

            System.out.println("===============================");

            System.out.println();

            for (Row row : sheet) {

                // Skip Header Row
                if (row.getRowNum() == 0) {
                    continue;
                }

                String org =
                        getCellValue(row.getCell(0));

                String phoneLabel =
                        getCellValue(row.getCell(1));

                String simNumber =
                        getCellValue(row.getCell(2));

                String mobileNumber =
                        getCellValue(row.getCell(3));

                String assignedEmployee =
                        getCellValue(row.getCell(4));

                String status =
                        getCellValue(row.getCell(5));

                String remarks =
                        getCellValue(row.getCell(6));

                System.out.println();

                System.out.println("========== SIM ==========");

                System.out.println("Organization      : " + org);

                System.out.println("Phone Label       : " + phoneLabel);

                System.out.println("SIM Number        : " + simNumber);

                System.out.println("Mobile Number     : " + mobileNumber);

                System.out.println("Assigned Employee : " + assignedEmployee);

                System.out.println("Status            : " + status);

                System.out.println("Remarks           : " + remarks);

            }

            workbook.close();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    /**
     * Safely returns the cell value as String.
     */
    private String getCellValue(Cell cell) {

        if (cell == null) {
            return "";
        }

        return cell.toString().trim();

    }

}