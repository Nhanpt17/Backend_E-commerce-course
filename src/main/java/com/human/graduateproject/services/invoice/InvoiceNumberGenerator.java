package com.human.graduateproject.services.invoice;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class InvoiceNumberGenerator {
    public String generateInvoiceNumber(Long orderId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String datePart = LocalDateTime.now().format(formatter);
        return "INV-" + datePart + "-" + String.format("%05d", orderId);
    }
}
