package com.human.graduateproject.mapper;

import com.human.graduateproject.dto.OrderItemDto;
import com.human.graduateproject.entity.Order;
import com.human.graduateproject.entity.OrderItem;
import com.human.graduateproject.entity.Product;

import java.util.List;
import java.util.stream.Collectors;

public class OrderItemMapper {
    public static OrderItemDto mapToOrderItemDto(OrderItem orderItem){
        if (orderItem ==null) return  null;

        return new OrderItemDto(
                orderItem.getId(),
                orderItem.getProduct().getId(),
                orderItem.getProduct().getName(),
                orderItem.getProduct().getPrice(),
                orderItem.getProduct().getCategory().getId(),
                orderItem.getProduct().getCategory().getName(),
                orderItem.getQuantity()
        );
    }

    public static List<OrderItemDto> mapToOrderItemDtoList(List<OrderItem> orderItems){
        return orderItems.stream()
                .map(OrderItemMapper::mapToOrderItemDto)
                .collect(Collectors.toList());
    }

//    public static OrderItem mapToOrderItem(OrderItemDto orderItemDto){
//        if (orderItemDto ==null) return null;
//
//        OrderItem orderItem = new OrderItem();
//        orderItem.setId(orderItemDto.getId());
//        orderItem.setQuantity(orderItemDto.getQuantity());
//        orderItem.setProduct(new Product());
//        orderItem.setOrder(new Order());
//        return  orderItem;
//    }

}
