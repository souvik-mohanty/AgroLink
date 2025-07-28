package com.agrolink.admin.service;

import com.agrolink.admin.model.User;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ReportService {

    public byte[] generatePdfReport(List<User> users) throws IOException {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(output);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("User Report").setBold().setFontSize(16));

            for (User user : users) {
                String line = String.format("Name: %s | Role: %s", user.getName(), user.getRole());
                document.add(new Paragraph(line));
            }

            document.close();
            return output.toByteArray();
        }
    }

    public byte[] generateExcelReport(List<User> users) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Users");

            // Header Row
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Name");
            header.createCell(1).setCellValue("Role");

            // User Rows
            int rowNum = 1;
            for (User user : users) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(user.getName());
                row.createCell(1).setCellValue(user.getRole());
            }

            // Auto-size columns
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);

            workbook.write(bos);
            return bos.toByteArray();
        }
    }
}
