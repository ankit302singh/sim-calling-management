package com.rocketlearning.simcallingmanagement.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.rocketlearning.simcallingmanagement.entity.SimData;

@Service
public class ExcelExportService {

    public byte[] exportToExcel(List<SimData> sims) throws IOException {

        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("SIM Data");

        Row header = sheet.createRow(0);

        header.createCell(0).setCellValue("Org");
        header.createCell(1).setCellValue("Phone Label");
        header.createCell(2).setCellValue("SIM Number");
        header.createCell(3).setCellValue("Mobile Number");
        header.createCell(4).setCellValue("Status");
        header.createCell(5).setCellValue("Assigned Employee");
        header.createCell(6).setCellValue("Remarks");

        int rowNum = 1;

        for (SimData sim : sims) {

            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(sim.getOrg());

            row.createCell(1).setCellValue(sim.getPhoneLabel());

            row.createCell(2).setCellValue(sim.getSimNumber());

            row.createCell(3).setCellValue(sim.getMobileNumber());

            row.createCell(4).setCellValue(
                    sim.getStatus() == null ? "" : sim.getStatus().name());

            row.createCell(5).setCellValue(sim.getAssignedEmployee());

            row.createCell(6).setCellValue(sim.getRemarks());
        }

        for (int i = 0; i < 7; i++) {

            sheet.autoSizeColumn(i);

        }

        ByteArrayOutputStream outputStream =
                new ByteArrayOutputStream();

        workbook.write(outputStream);

        workbook.close();

        return outputStream.toByteArray();

    }

}