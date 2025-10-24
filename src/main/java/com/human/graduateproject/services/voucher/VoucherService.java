package com.human.graduateproject.services.voucher;

import com.human.graduateproject.entity.CustomerVoucher;
import com.human.graduateproject.entity.Point;
import com.human.graduateproject.entity.Voucher;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface VoucherService {
    Voucher createVoucher(Voucher voucher);
    Voucher updateVoucher(Long voucherId,Voucher voucherDetails);
    void deleteVoucher(Long voucherId);
    List<Voucher> getAllVouchers();

    List<Voucher> getPublicVouchers();

    Voucher redeemVoucher(Long customerId, Long voucherId);

    Point getCustomerPoints(Long customerId);

    @Transactional
    Point redeemPointsForVoucher(Long customerId, Long voucherId, Integer pointsRequired);

//    List<CustomerVoucher> getCustomerVouchers(Long customerId);
    List<CustomerVoucher> getCustomerVouchers(Long customerId);


    void markCustomerVoucherAsUsed(Long customerId, Long voucherId);

    void incrementVoucherUsage(String voucherCode);
}
