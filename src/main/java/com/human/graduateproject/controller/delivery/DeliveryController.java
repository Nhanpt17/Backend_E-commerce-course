package com.human.graduateproject.controller.delivery;

import com.human.graduateproject.dto.OrderDto;
import com.human.graduateproject.dto.UserDto;
import com.human.graduateproject.services.delivery.DeliveryService;
import com.human.graduateproject.util.OrderStatusUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/delivery")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @PutMapping("/status/{id}")
     public ResponseEntity<OrderDto> updateOrderStatus(@PathVariable Long id, @RequestBody OrderStatusUpdateRequest request){
        OrderDto orderDto = deliveryService.updateOrderStatus(id,request);
        return ResponseEntity.ok(orderDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<OrderDto>> getOrdersByDeliveryStaff(@PathVariable Long id){
        List<OrderDto> orderDtoList = deliveryService.getOrdersByDeliveryStaff(id);
        return ResponseEntity.ok(orderDtoList);
    }



}
