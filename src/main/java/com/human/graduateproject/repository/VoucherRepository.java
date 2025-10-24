package com.human.graduateproject.repository;

import com.human.graduateproject.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher,Long> {
    Optional<Voucher> findByCode(String code);

    @Transactional
    @Modifying
    @Query("UPDATE Voucher v SET v.isActive  = false WHERE v.id = :voucherId")
    int softDeleteVoucher(@Param("voucherId") Long voucherId);

    @Transactional
    @Modifying
    @Query("UPDATE Voucher v SET v.currentUsage = v.currentUsage + 1 WHERE v.code = :code")
    int incrementUsageByCode(@Param("code") String code);


    List<Voucher> findByIsPublicTrueAndEndDateAfter(LocalDateTime date);




}
