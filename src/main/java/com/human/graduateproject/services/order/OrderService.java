package com.human.graduateproject.services.order;

import com.human.graduateproject.dto.OrderDto;
import com.human.graduateproject.entity.CreateOrderRequest;
import com.human.graduateproject.entity.Order;

import java.util.List;

public interface OrderService {
    OrderDto createOrder(CreateOrderRequest request);
    List<OrderDto> getOrdersByUser(Long userId);

    Order getOrderById(Long orderId);

    void cancelOrder(Long orderId);

    void cancelOrderByStaff(Long orderId);
}
