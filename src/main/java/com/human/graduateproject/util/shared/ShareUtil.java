package com.human.graduateproject.util.shared;

import com.human.graduateproject.entity.Voucher;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ShareUtil {
    public static int calculatePointsEarned(BigDecimal amount) {
        return amount.divide(BigDecimal.valueOf(1000), 0, RoundingMode.DOWN).intValue();
    }

    public static double calculateDiscountAmount(Voucher voucher, double totalPrice, double shippingFee) {
        return switch (voucher.getDiscountType()) {
            case PERCENTAGE -> totalPrice * voucher.getDiscountValue().doubleValue() / 100;
            case FIXED_AMOUNT -> Math.min(voucher.getDiscountValue().doubleValue(), totalPrice);
            case SHIPPING_PERCENTAGE -> shippingFee * voucher.getDiscountValue().doubleValue() / 100;
            default -> 0;
        };
    }


}
