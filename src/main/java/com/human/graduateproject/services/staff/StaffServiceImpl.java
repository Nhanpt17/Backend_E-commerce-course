package com.human.graduateproject.services.staff;

import com.human.graduateproject.dto.InventoryCheckResponse;
import com.human.graduateproject.dto.OrderDto;
import com.human.graduateproject.entity.Order;
import com.human.graduateproject.entity.Users;
import com.human.graduateproject.enums.OrderStatus;
import com.human.graduateproject.mapper.OrderMapper;
import com.human.graduateproject.repository.OrderRepository;
import com.human.graduateproject.repository.UserRepository;
import com.human.graduateproject.services.inventory.InventoryService;
import com.human.graduateproject.util.OrderStatusUpdateRequest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StaffServiceImpl implements StaffService{

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final InventoryService inventoryService;

    public StaffServiceImpl(OrderRepository orderRepository, UserRepository userRepository, InventoryService inventoryService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.inventoryService = inventoryService;
    }





    @Override
    public void assignDeliveryStaff(Long orderId, Long deliveryId, Long staffId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(()-> new EntityNotFoundException("Order not found"));

        // Kiểm tra kho hàng trước khi phân công
        InventoryCheckResponse inventoryCheck = inventoryService.checkInventory(order);
        if (!inventoryCheck.isAvailable()) {
            throw new RuntimeException(inventoryCheck.getMessage());
        }

        Users delivery = userRepository.findById(deliveryId).orElseThrow(()-> new EntityNotFoundException("Delivery not found"));
        Users staff = userRepository.findById(staffId).orElseThrow(()-> new EntityNotFoundException("Staff not found"));
        order.setDeliveryStaff(delivery);
        order.setProcessingStaff(staff);
        order.setProcessedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.PLACED);
        orderRepository.save(order);

        inventoryService.deductInventory(order);

    }


    @Override
    public Page<OrderDto> getAllOrdersForStaff(Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(pageable);
        return orders.map(OrderMapper :: mapToOrderDto);
    }


}
