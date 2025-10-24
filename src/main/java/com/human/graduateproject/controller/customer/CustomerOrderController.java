package com.human.graduateproject.controller.customer;

import com.human.graduateproject.dto.OrderDto;
import com.human.graduateproject.entity.CreateOrderRequest;
import com.human.graduateproject.services.order.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer/orders")
public class CustomerOrderController {
    private final OrderService orderService;


    public CustomerOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody CreateOrderRequest request){
        OrderDto orderDto = orderService.createOrder(request);
        return ResponseEntity.ok(orderDto);
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<OrderDto> > getOrdersByUserId(@PathVariable Long userId){
        List<OrderDto> orderDtos = orderService.getOrdersByUser(userId);

        return ResponseEntity.ok(orderDtos);
        
    }
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Long orderId) {
        try {
           orderService.cancelOrder(orderId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
