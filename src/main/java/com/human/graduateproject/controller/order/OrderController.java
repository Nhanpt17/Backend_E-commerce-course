package com.human.graduateproject.controller.order;

import com.human.graduateproject.dto.OrderDto;
import com.human.graduateproject.entity.Order;
import com.human.graduateproject.mapper.OrderMapper;
import com.human.graduateproject.repository.UserRepository;
import com.human.graduateproject.services.order.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.html.Option;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/order")
public class OrderController {
    private final OrderService orderService;
    private final UserRepository userRepository;

    public OrderController(OrderService orderService, UserRepository userRepository) {
        this.orderService = orderService;
        this.userRepository = userRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id){
        Order order = orderService.getOrderById(id);
        OrderDto orderDto = OrderMapper.mapToOrderDto(order);

        Map<String, Object> response = new HashMap<>();
        response.put("order", orderDto);

        if (order.getDeliveryStaff() != null) {
            String deliveryName = userRepository.findNameById(order.getDeliveryStaff().getId())
                    .orElse(null);
            response.put("deliveryName", deliveryName);
        }

        return ResponseEntity.ok(response);
    }
}
