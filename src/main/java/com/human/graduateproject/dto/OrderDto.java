package com.human.graduateproject.dto;

import com.human.graduateproject.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Long id;
    private Long userId;
    private LocalDateTime orderDate;
    private String shippingAddress;
    private String phoneNumber;
    private String customerName;
    private String email;
    private BigDecimal totalPrice;
    private BigDecimal shippingFee;
    private BigDecimal discountAmount;
    private BigDecimal finalAmount;
    private OrderStatus status;
    private List<OrderItemDto> items;
    private PaymentDto payment;
    private Long processingStaffId;
    private Long deliveryStaffId;
    private LocalDateTime processedAt;  // Thời điểm xử lý đơn
    private LocalDateTime deliveryAt;   // Thời điểm bắt đầu giao
    private LocalDateTime completedAt;  // Thời điểm hoàn thành

}
