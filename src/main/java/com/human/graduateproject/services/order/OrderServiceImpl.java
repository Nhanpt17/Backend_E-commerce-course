package com.human.graduateproject.services.order;

import com.human.graduateproject.dto.OrderDto;
import com.human.graduateproject.entity.*;
import com.human.graduateproject.enums.OrderStatus;
import com.human.graduateproject.enums.PaymentMethod;
import com.human.graduateproject.enums.PaymentStatus;
import com.human.graduateproject.enums.TransactionType;
import com.human.graduateproject.mapper.OrderMapper;
import com.human.graduateproject.repository.*;

import com.human.graduateproject.util.shared.ShareUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final PointRepository pointRepository;
    private final PointTransactionRepository pointTransactionRepository;

    public OrderServiceImpl(OrderRepository orderRepository, PaymentRepository paymentRepository, UserRepository userRepository, PointRepository pointRepository, PointTransactionRepository pointTransactionRepository) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.pointRepository = pointRepository;
        this.pointTransactionRepository = pointTransactionRepository;
    }

    @Override
    public OrderDto createOrder(CreateOrderRequest request) {
        // 1. validate data
        if (request.getFinalAmount().compareTo(BigDecimal.ZERO) <=0){
            throw new IllegalArgumentException("Số tiền thanh toán không hợp lệ");
        }

        // 2. create order
        Order order = new Order();
        order.setCustomerName(request.getCustomerName());
        order.setCustomerEmail(request.getCustomerEmail());
        order.setAddress(request.getAddress());
        order.setPhone(request.getPhone());
        order.setStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalPrice(request.getTotalPrice());
        order.setShippingFee(request.getShippingFee());
        order.setDiscountAmount(request.getDiscountAmount());
        order.setFinalAmount(request.getFinalAmount());

        // 3. Handling customer
        Users customer = userRepository.findById(request.getCustomerId())
                .orElseThrow(()->new EntityNotFoundException("Không tìm thấy khách hàng"));
        order.setUsers(customer);


        // 4. Move cartItems -> orderItems
        List<OrderItem> orderItems = request.getCartItems().stream()
                .map(cartItem ->{
                    OrderItem item = new OrderItem();

                    Product product = new Product(
                            cartItem.getId(),
                            cartItem.getName(),
                            cartItem.getPrice(),
                            null,
                            null,
                            new Category(cartItem.getCategoryId(), cartItem.getCategoryName(), null)
                    );
                    item.setProduct(product);
                    item.setQuantity(cartItem.getQuantity());

                    item.setOrder(order);
                    return item;
                        }

                ).toList();

        order.setOrderItems(orderItems);

        // 5. Save Order
        Order saveOrder = orderRepository.save(order);

        // 6. create Payment
        Payment payment = new Payment();
        payment.setOrder(saveOrder);
        payment.setAmount(saveOrder.getFinalAmount());
        payment.setPaymentMethod(Objects.equals(request.getPayment(), "cash") ? PaymentMethod.CASH:PaymentMethod.MOMO);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setStatus(PaymentStatus.PENDING);

        paymentRepository.save(payment);
        saveOrder.setPayment(payment);

        return OrderMapper.mapToOrderDto(saveOrder);
    }

    @Override
    public List<OrderDto> getOrdersByUser(Long userId) {

       List<Order> orders = orderRepository.findByUsers_Id(userId);

        if (orders.isEmpty()) {
            throw new EntityNotFoundException("Không tìm thấy order nào với userId: " + userId);
        }


        return orders.stream()
                .map(OrderMapper::mapToOrderDto)
                .collect(Collectors.toList());
    }

    @Override
    public Order getOrderById(Long orderId) {

        return orderRepository.findById(orderId).orElseThrow(
                ()->new EntityNotFoundException("Order not found")
        );

    }

    @Override
    public void cancelOrder(Long orderId) {

        int updatedRows = orderRepository.updateOrderStatusIfPendingOrPlaced(orderId, OrderStatus.CANCELLED);
        if (updatedRows == 0) {
            throw new RuntimeException("Order cannot be cancelled");
        }

    }

    @Override
    public void cancelOrderByStaff(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(()-> new EntityNotFoundException("Order not found"));
        Long customerId =order.getUsers().getId();

        int updatedRows = orderRepository.updateOrderStatusIfPendingOrPlaced(orderId, OrderStatus.CANCELLED);
        if (updatedRows == 0) {
            throw new RuntimeException("Order cannot be cancelled");
        }


        if (order.getDiscountAmount().intValue() >0){


            int pointCompensation = ShareUtil.calculatePointsEarned(order.getDiscountAmount());

           Point point = pointRepository.findByCustomerId(customerId)
                    .orElseGet(()->{
                        Point newPoint = new Point();
                        newPoint.setCustomerId(customerId);
                        return pointRepository.save(newPoint);
                    });

           point.setBalance(point.getBalance() + pointCompensation );

           PointTransaction pointTransaction = new PointTransaction();
           pointTransaction.setTransactionType(TransactionType.ADJUST);
           pointTransaction.setCustomerId(customerId);
           pointTransaction.setReferenceType("ORDER");
           pointTransaction.setReferenceId(orderId);
           pointTransaction.setPointsChange(+pointCompensation);

           pointRepository.save( point);
           pointTransactionRepository.save(pointTransaction);
        }

    }


}
