package com.human.graduateproject.payload;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ApplyVoucherResponse extends ApiResponse{
    private double discountAmount;

    public ApplyVoucherResponse(boolean success, String message, double discountAmount) {
        super(success, message);
        this.discountAmount = discountAmount;
    }
}
