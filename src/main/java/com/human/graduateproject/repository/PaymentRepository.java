package com.human.graduateproject.repository;

import com.human.graduateproject.entity.Payment;
import com.human.graduateproject.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {

    @Transactional
    @Modifying
    @Query("UPDATE Payment p SET p.status = :status, p.transactionId = :txnId, p.paymentDate = :paidAt WHERE p.order.id = :orderId")
    void updatePaymentStatus(@Param("orderId") Long orderId,
                             @Param("status") PaymentStatus status,
                             @Param("txnId") String txnId,
                             @Param("paidAt") LocalDateTime paidAt);


}
