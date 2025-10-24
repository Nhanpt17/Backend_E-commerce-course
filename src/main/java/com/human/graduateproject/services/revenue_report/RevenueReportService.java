package com.human.graduateproject.services.revenue_report;

import java.util.Map;

public interface RevenueReportService {
    Map<String, Object> getRevenueReport(String startDate, String endDate);
}
