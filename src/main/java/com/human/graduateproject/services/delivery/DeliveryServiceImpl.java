package com.human.graduateproject.services.delivery;

import com.human.graduateproject.dto.OrderDto;
import com.human.graduateproject.entity.*;
import com.human.graduateproject.enums.*;
import com.human.graduateproject.mapper.OrderMapper;
import com.human.graduateproject.payload.DeliveryStaffView;
import com.human.graduateproject.repository.*;
import com.human.graduateproject.services.invoice.InvoiceService;
import com.human.graduateproject.services.voucher.VoucherService;
import com.human.graduateproject.util.OrderStatusUpdateRequest;
import com.human.graduateproject.util.shared.ShareUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeliveryServiceImpl implements DeliveryService{

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final InvoiceService invoiceService;
    private final VoucherService voucherService;
    private final PointRepository pointRepository;
    private final PointTransactionRepository pointTransactionRepository;

    public DeliveryServiceImpl(OrderRepository orderRepository, UserRepository userRepository, InvoiceService invoiceService, VoucherService voucherService, PointRepository pointRepository, PointTransactionRepository pointTransactionRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.invoiceService = invoiceService;
        this.voucherService = voucherService;
        this.pointRepository = pointRepository;
        this.pointTransactionRepository = pointTransactionRepository;
    }

    @Override
    public List<OrderDto> getOrdersByDeliveryStaff(Long deliveryStaffId) {

         List<Order> orders = orderRepository.findByDeliveryStaff_Id(deliveryStaffId);
        if(orders.isEmpty()){
            throw new EntityNotFoundException("Order not found");
        }

        return orders.stream()
                .map(OrderMapper::mapToOrderDto)
                .collect(Collectors.toList());

    }

    @Override
    public OrderDto updateOrderStatus(Long orderId, OrderStatusUpdateRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()-> new EntityNotFoundException("Order not found"));

        if (request.getStatus() == OrderStatus.COMPLETED ) {
           Order completedOrder =   completeOrder(order);

           earnPoints(completedOrder.getUsers().getId(),completedOrder);

            return OrderMapper.mapToOrderDto(completedOrder);
        }

        order.setDeliveryAt(LocalDateTime.now());
        order.setStatus(request.getStatus());

        Order updatedOrder = orderRepository.save(order);

        return OrderMapper.mapToOrderDto(updatedOrder);
    }

    @Transactional
    @Override
    public Order completeOrder(Order order) {

        if (!OrderStatus.DELIVERING.equals(order.getStatus())) {

            throw new RuntimeException("Order must be in DELIVERING state");
        }

        if(order.getPayment().getPaymentMethod() == PaymentMethod.CASH){
            order.getPayment().setPaymentDate(LocalDateTime.now());
            order.getPayment().setStatus(PaymentStatus.PAID);
        }

        order.setStatus(OrderStatus.COMPLETED);
        order.setCompletedAt(LocalDateTime.now());

        // Tạo hóa đơn tự động
        invoiceService.createInvoiceFromOrder(order);

        return orderRepository.save(order);
    }


    @Override
    public List<DeliveryStaffView> getDeliveryStaffSummary() {
        return userRepository.findDeliveryStaffSummaryByRole(UserRole.DELIVERY);
    }

    @Override
    public void earnPoints(Long customerId, Order order){
        Point point = voucherService.getCustomerPoints(customerId);

        int pointEarn = ShareUtil.calculatePointsEarned(order.getFinalAmount());

        point.setBalance(point.getBalance() + pointEarn);
        point.setLastUpdated(LocalDateTime.now());
        pointRepository.save(point);

        PointTransaction transaction = new PointTransaction();
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setCustomerId(customerId);
        transaction.setPointsChange(pointEarn);
        transaction.setTransactionType(TransactionType.ERN);
        transaction.setReferenceId(order.getId());
        transaction.setReferenceType("ORDER");
        pointTransactionRepository.save(transaction);

    }



}
