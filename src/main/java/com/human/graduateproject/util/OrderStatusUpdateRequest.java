package com.human.graduateproject.util;

import com.human.graduateproject.enums.OrderStatus;
import lombok.Data;

@Data
public class OrderStatusUpdateRequest {
    private OrderStatus status;
}
