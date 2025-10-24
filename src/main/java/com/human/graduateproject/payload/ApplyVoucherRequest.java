package com.human.graduateproject.payload;

import lombok.Data;

@Data
public class ApplyVoucherRequest {
    private String code;
    private double totalPrice;
    private double shippingFee;
}
