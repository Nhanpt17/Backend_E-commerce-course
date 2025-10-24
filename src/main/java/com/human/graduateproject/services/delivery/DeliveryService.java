package com.human.graduateproject.services.delivery;

import com.human.graduateproject.dto.OrderDto;
import com.human.graduateproject.dto.UserDto;
import com.human.graduateproject.entity.Order;
import com.human.graduateproject.payload.DeliveryStaffView;
import com.human.graduateproject.util.OrderStatusUpdateRequest;
import jakarta.transaction.Transactional;

import java.util.List;

public interface DeliveryService {
    List<OrderDto> getOrdersByDeliveryStaff(Long deliveryStaffId);

    OrderDto updateOrderStatus(Long orderId, OrderStatusUpdateRequest request);

    @Transactional
    Order completeOrder(Order order);

    List<DeliveryStaffView> getDeliveryStaffSummary();

    void earnPoints(Long customerId, Order order);
}
