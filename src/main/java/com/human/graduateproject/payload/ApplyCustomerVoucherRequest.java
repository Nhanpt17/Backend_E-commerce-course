package com.human.graduateproject.payload;

import lombok.Data;

@Data
public class ApplyCustomerVoucherRequest {
    private Long customerId;
    private Long voucherId;
    private double totalPrice;
    private double shippingFee;
}
