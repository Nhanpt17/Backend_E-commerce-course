package com.human.graduateproject.services.staff;

import com.human.graduateproject.dto.OrderDto;
import com.human.graduateproject.entity.Order;
import com.human.graduateproject.entity.Users;
import com.human.graduateproject.util.OrderStatusUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface StaffService {


    void assignDeliveryStaff(Long orderId, Long deliveryId,Long staffId);



    Page<OrderDto> getAllOrdersForStaff(Pageable pageable);
}
