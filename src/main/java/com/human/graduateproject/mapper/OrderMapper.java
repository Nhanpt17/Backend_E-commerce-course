package com.human.graduateproject.mapper;

import com.human.graduateproject.dto.OrderDto;
import com.human.graduateproject.entity.Order;
import com.human.graduateproject.entity.OrderItem;
import com.human.graduateproject.entity.Users;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class OrderMapper {
    public static OrderDto mapToOrderDto(Order order){
        if (order == null) return null;

        Long userId = order.getUsers() != null ? order.getUsers().getId() : null;
        Long processingStaffId = order.getProcessingStaff() != null ? order.getProcessingStaff().getId() : null;
        Long deliveryStaffId = order.getDeliveryStaff() != null ? order.getDeliveryStaff().getId() : null;


        return new OrderDto(
                order.getId(),
                userId,
                order.getOrderDate(),
                order.getAddress(),
                order.getPhone(),
                order.getCustomerName(),
                order.getCustomerEmail(),
                order.getTotalPrice(),
                order.getShippingFee(),
                order.getDiscountAmount(),
                order.getFinalAmount(),
                order.getStatus(),
                OrderItemMapper.mapToOrderItemDtoList(order.getOrderItems()),
                PaymentMapper.mapToPaymentDto(order.getPayment()),
                processingStaffId,
                deliveryStaffId,
                order.getProcessedAt(),
                order.getDeliveryAt(),
                order.getCompletedAt()
        );
    }

//    public static Order mapToOrder(OrderDto orderDto){
//        if (orderDto ==null) return  null;
//
//        Order order = new Order();
//        order.setId(orderDto.getId());
//        order.setOrderDate(orderDto.getOrderDate());
//        order.setAddress(orderDto.getShippingAddress());
//        order.setPhone(orderDto.getPhoneNumber());
//        order.setCustomerName(orderDto.getCustomerName());
//        order.setCustomerEmail(orderDto.getEmail());
//        order.setTotalPrice(orderDto.getTotalPrice());
//        order.setDiscountAmount(orderDto.getDiscountAmount());
//        order.setShippingFee(orderDto.getShippingFee());
//        order.setFinalAmount(orderDto.getFinalAmount());
//        order.setStatus(orderDto.getStatus());
//        order.setPayment(PaymentMapper.mapToPayment(orderDto.getPayment()));
//        order.setUsers(new Users());
//        order.setOrderItems(new ArrayList<OrderItem>());
//
//        return  order;
//
//    }

}
