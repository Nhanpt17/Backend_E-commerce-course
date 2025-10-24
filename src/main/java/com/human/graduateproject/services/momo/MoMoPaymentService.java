package com.human.graduateproject.services.momo;


import com.human.graduateproject.dto.OrderDto;
import com.human.graduateproject.entity.Order;
import com.human.graduateproject.entity.Payment;
import com.human.graduateproject.enums.OrderStatus;
import com.human.graduateproject.enums.PaymentStatus;
import com.human.graduateproject.mapper.OrderMapper;
import com.human.graduateproject.repository.OrderRepository;
import com.human.graduateproject.repository.PaymentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

@Service
@Transactional
public class MoMoPaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    public MoMoPaymentService(OrderRepository orderRepository, PaymentRepository paymentRepository) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
    }

    public boolean updateOrderAfterMomoSuccess(Long orderId, String momoTransactionId) {
        // 1. Tìm đơn hàng
        boolean orderExists = orderRepository.existsById(orderId);
        if (!orderExists) {
            throw new EntityNotFoundException("Order not found");
        }
        // 2. Cập nhật trạng thái


        // 3. Cập nhật payment


        paymentRepository.updatePaymentStatus(
                orderId,
                PaymentStatus.PAID,
                momoTransactionId,
                LocalDateTime.now()
        );

        return true;

    }

    public boolean updateOrderAfterMomoFailed(Long orderId, String momoTransactionId) {
        // 1. Tìm đơn hàng
        boolean orderExists = orderRepository.existsById(orderId);
        if (!orderExists) {
            throw new EntityNotFoundException("Order not found");
        }
        // 2. Cập nhật trạng thái
        orderRepository.updateOrderStatus(orderId,OrderStatus.CANCELLED);

        // 3. Cập nhật payment

        paymentRepository.updatePaymentStatus(orderId,PaymentStatus.FAILED,momoTransactionId,LocalDateTime.now());


        return true;
    }


}
