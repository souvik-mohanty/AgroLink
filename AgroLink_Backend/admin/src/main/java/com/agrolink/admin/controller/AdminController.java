package com.agrolink.admin.controller;

import com.agrolink.admin.model.User;
import com.agrolink.admin.model.Complaint;
import com.agrolink.admin.repository.UserRepository;
import com.agrolink.admin.service.AdminService;
import com.agrolink.admin.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final ReportService reportService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello from Admin Service");
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return adminService.getAllUsers();
    }

    @PostMapping("/suspend/{id}")
    public ResponseEntity<?> suspendUser(@PathVariable String id) {
        adminService.suspendUser(id);
        return ResponseEntity.ok("User suspended");
    }

    @PostMapping("/approve-listing/{id}")
    public ResponseEntity<?> approveListing(@PathVariable String id) {
        adminService.approveListing(id);
        return ResponseEntity.ok("Listing approved");
    }

    @GetMapping("/complaints")
    public List<Complaint> getComplaints() {
        return adminService.getComplaints();
    }

    @PostMapping("/complaints/{id}/resolve")
    public ResponseEntity<?> resolveComplaint(@PathVariable String id) {
        adminService.resolveComplaint(id);
        return ResponseEntity.ok("Complaint resolved");
    }

    @GetMapping("/report/pdf")
    public ResponseEntity<byte[]> getPdfReport() throws IOException {
        byte[] report = reportService.generatePdfReport(userRepository.findAll());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.pdf")
                .body(report);
    }

    @GetMapping("/report/excel")
    public ResponseEntity<byte[]> getExcelReport() throws IOException {
        byte[] report = reportService.generateExcelReport(userRepository.findAll());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.xlsx")
                .body(report);
    }
}
