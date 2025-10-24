package com.human.graduateproject.controller.admin;

import com.human.graduateproject.services.revenue_report.RevenueReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/reports")
public class AdminReportController {
    private final RevenueReportService revenueReportService;


    public AdminReportController(RevenueReportService revenueReportService) {
        this.revenueReportService = revenueReportService;
    }

    @GetMapping("/revenue")
    public ResponseEntity<Map<String, Object>> getRevenueReport(
            @RequestParam  String startDate,
            @RequestParam String endDate) {

        Map<String, Object> report = revenueReportService.getRevenueReport(startDate, endDate);
        return ResponseEntity.ok(report);
    }
}
