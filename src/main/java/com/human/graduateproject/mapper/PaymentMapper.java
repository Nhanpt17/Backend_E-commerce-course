package com.human.graduateproject.mapper;

import com.human.graduateproject.dto.PaymentDto;
import com.human.graduateproject.entity.Order;
import com.human.graduateproject.entity.Payment;

public class PaymentMapper {
    public static PaymentDto mapToPaymentDto(Payment payment){
        if (payment==null) return null;
        return new PaymentDto(
                payment.getId(),
                payment.getOrder().getId(),
                payment.getAmount(),
                payment.getPaymentDate(),
                payment.getPaymentMethod(),
                payment.getStatus(),
                payment.getTransactionId()
        );
    }

//    public static Payment mapToPayment(PaymentDto paymentDto){
//        if (paymentDto ==null) return null;
//
//        Payment payment = new Payment();
//        payment.setId(paymentDto.getId());
//        payment.setPaymentDate(paymentDto.getPaymentDate());
//        payment.setPaymentMethod(paymentDto.getPaymentMethod());
//        payment.setStatus(paymentDto.getStatus());
//
//        payment.setOrder( new Order());
//        payment.setAmount(paymentDto.getAmount());
//        payment.setTransactionId(paymentDto.getTransactionId());
//        return  payment;
//
//    }
}
