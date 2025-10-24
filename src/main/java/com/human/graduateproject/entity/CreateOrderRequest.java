package com.human.graduateproject.entity;

import com.human.graduateproject.dto.OrderItemDto;
import com.human.graduateproject.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {

    private Long customerId;
    private String customerName;
    private String customerEmail;
    private List<OrderItemDto> cartItems; // Khớp với data gửi lên
    private BigDecimal totalPrice;
    private BigDecimal shippingFee;
    private BigDecimal discountAmount;
    private BigDecimal finalAmount;
    private String phone;
    private String address;
    private String payment;


}

