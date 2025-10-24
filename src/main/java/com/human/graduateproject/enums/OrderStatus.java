package com.human.graduateproject.enums;

public enum OrderStatus {

    PENDING,    // Đơn mới đặt, chờ quán xác nhận
    PLACED,    // Đã đặt hàng
    DELIVERING, // Shipper đang giao
    COMPLETED,  // Giao thành công
    CANCELLED   // Hủy đơn (khách/quán)
}
