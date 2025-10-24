package com.human.graduateproject.services.revenue_report;

import com.human.graduateproject.repository.InvoiceRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RevenueReportServiceImpl  implements RevenueReportService{


    private final InvoiceRepository invoiceRepository;

    public RevenueReportServiceImpl(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public Map<String, Object> getRevenueReport(String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(23, 59, 59);

        System.out.println("Start: " + startDateTime);
        System.out.println("End: " + endDateTime);
        Map<String, Object> report = new HashMap<>();

        // Tổng doanh thu trong khoảng thời gian
        BigDecimal totalRevenue = invoiceRepository.getTotalRevenueBetweenDates(startDateTime, endDateTime);
        report.put("totalRevenue", totalRevenue != null ? totalRevenue : BigDecimal.ZERO);

        // Doanh thu theo ngày
        List<Map<String, Object>> dailyRevenues = invoiceRepository.getDailyRevenueBetweenDates(startDateTime, endDateTime)
                .stream()
                .map(obj -> {
                    Map<String, Object> daily = new HashMap<>();
                    daily.put("date", obj[0]);
                    daily.put("amount", obj[1]);
                    return daily;
                })
                .collect(Collectors.toList());
        report.put("dailyRevenues", dailyRevenues);

        // Doanh thu theo tháng
        List<Map<String, Object>> monthlyRevenues = invoiceRepository.getMonthlyRevenueBetweenDates(startDateTime, endDateTime)
                .stream()
                .map(obj -> {
                    Map<String, Object> monthly = new HashMap<>();
                    monthly.put("year", obj[0]);
                    monthly.put("month", obj[1]);
                    monthly.put("amount", obj[2]);
                    return monthly;
                })
                .collect(Collectors.toList());
        report.put("monthlyRevenues", monthlyRevenues);

        // Doanh thu theo phương thức thanh toán
        List<Map<String, Object>> paymentMethodRevenues = invoiceRepository.getRevenueByPaymentMethodBetweenDates(startDateTime, endDateTime)
                .stream()
                .map(obj -> {
                    Map<String, Object> method = new HashMap<>();
                    method.put("paymentMethod", obj[0]);
                    method.put("amount", obj[1]);
                    return method;
                })
                .collect(Collectors.toList());
        report.put("paymentMethodRevenues", paymentMethodRevenues);

        return report;
    }
}
