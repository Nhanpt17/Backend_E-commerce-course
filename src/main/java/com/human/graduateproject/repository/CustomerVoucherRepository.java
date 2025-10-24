package com.human.graduateproject.repository;

import com.human.graduateproject.entity.CustomerVoucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerVoucherRepository  extends JpaRepository<CustomerVoucher,Long> {


    Optional<CustomerVoucher> findByCustomerIdAndVoucher_Id(Long customerId, Long voucherId);


    @Modifying
    @Transactional
    @Query("UPDATE CustomerVoucher cv SET cv.used = true, cv.usedAt = :usedAt " +
            "WHERE cv.customerId = :customerId AND cv.voucher.id = :voucherId")
    int markAsUsed(@Param("customerId") Long customerId,
                   @Param("voucherId") Long voucherId,
                   @Param("usedAt") LocalDateTime usedAt);

    List<CustomerVoucher> findByCustomerIdAndUsedFalse(Long customerId);
}
