package com.human.graduateproject.repository;

import com.human.graduateproject.dto.OrderDto;
import com.human.graduateproject.entity.Order;
import com.human.graduateproject.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {


    List<Order> findByUsers_Id(Long userId);

    List<Order> findByDeliveryStaff_Id(Long deliveryStaffId);

    @Transactional
    @Modifying
    @Query("UPDATE Order o SET o.status = :status WHERE o.id = :orderId")
    void updateOrderStatus(@Param("orderId") Long orderId, @Param("status") OrderStatus status);

    @Transactional
    @Modifying
    @Query("UPDATE Order o SET o.status = :status WHERE o.id = :orderId AND (o.status = 'PENDING' OR o.status = 'PLACED')")
    int updateOrderStatusIfPendingOrPlaced(@Param("orderId") Long orderId, @Param("status") OrderStatus status);

    @Query("SELECT o.users.id FROM Order o WHERE o.id = :orderId")
    int selectCustomerIdByOrderId(@Param("orderId") Long orderId);

}
