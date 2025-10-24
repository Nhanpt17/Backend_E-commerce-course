package com.human.graduateproject.controller.staff;

import com.human.graduateproject.dto.InventoryCheckResponse;
import com.human.graduateproject.dto.OrderDto;
import com.human.graduateproject.dto.UserDto;
import com.human.graduateproject.entity.Order;
import com.human.graduateproject.payload.DeliveryStaffView;
import com.human.graduateproject.services.delivery.DeliveryService;
import com.human.graduateproject.services.inventory.InventoryService;
import com.human.graduateproject.services.order.OrderService;
import com.human.graduateproject.services.staff.StaffService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

    private final StaffService staffService;
    private final InventoryService inventoryService;
    private final OrderService orderService;
    private final DeliveryService deliveryService;

    public StaffController(StaffService staffService, InventoryService inventoryService, OrderService orderService, DeliveryService deliveryService) {
        this.staffService = staffService;
        this.inventoryService = inventoryService;
        this.orderService = orderService;
        this.deliveryService = deliveryService;
    }

    @GetMapping
    public ResponseEntity<Page<OrderDto>> getAllOrdersForStaff(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page,size, Sort.by("orderDate").descending());
        Page<OrderDto> orders = staffService.getAllOrdersForStaff(pageable);
//        List<OrderDto> orderDtoLists = staffService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @PutMapping("process/{staffId}/assign-delivery/{deliveryId}")
    public  ResponseEntity<?> assignDeliveryStaff(
            @PathVariable Long deliveryId, @PathVariable Long staffId,@RequestBody Long orderId){
        staffService.assignDeliveryStaff(orderId,deliveryId,staffId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/check-inventory/{orderId}")
    public ResponseEntity<InventoryCheckResponse> checkInventory(@PathVariable Long orderId){
        Order order = orderService.getOrderById(orderId);

        InventoryCheckResponse inventoryCheckResponse = inventoryService.checkInventory(order);
        return ResponseEntity.ok(inventoryCheckResponse);
    }

    @GetMapping("/delivery")
    public ResponseEntity<List<DeliveryStaffView>> getDeliveryStaff(){

        return ResponseEntity.ok(deliveryService.getDeliveryStaffSummary());
    }

    @PutMapping("/{orderId}/cancel-by-staff")
    public ResponseEntity<?> cancelOrderByStaff(@PathVariable Long orderId) {

        try {
            orderService.cancelOrderByStaff(orderId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
