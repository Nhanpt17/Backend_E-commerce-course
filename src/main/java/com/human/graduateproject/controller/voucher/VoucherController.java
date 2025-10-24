package com.human.graduateproject.controller.voucher;

import com.human.graduateproject.entity.Voucher;
import com.human.graduateproject.payload.ApiResponse;
import com.human.graduateproject.payload.ApplyVoucherRequest;
import com.human.graduateproject.payload.ApplyVoucherResponse;
import com.human.graduateproject.repository.VoucherRepository;
import com.human.graduateproject.util.shared.ShareUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class VoucherController {

    private  final VoucherRepository voucherRepository;

    public VoucherController(VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
    }


    @PostMapping("/vouchers/apply")
    public ResponseEntity<?> applyVoucher(@RequestBody ApplyVoucherRequest request) {
        try {
            // 1. Kiểm tra voucher có tồn tại và còn hiệu lực không

            Voucher voucher = voucherRepository.findByCode(request.getCode())
                    .orElseThrow(() -> new RuntimeException("Mã voucher không tồn tại"));

            if(voucher.isPublic()){
                return ResponseEntity.badRequest().body(
                        new ApiResponse(false, "Voucher này phải đổi bằng điểm")
                );
            }
            // 2. Kiểm tra thời gian hiệu lực
            LocalDateTime now = LocalDateTime.now();
            if (now.isBefore(voucher.getStartDate()) || now.isAfter(voucher.getEndDate())) {
                return ResponseEntity.badRequest().body(
                        new ApiResponse(false, "Voucher đã hết hạn hoặc chưa đến thời gian sử dụng")
                );
            }

            // 3. Kiểm tra số lần sử dụng
            if (voucher.getMaxUsage() != null && voucher.getCurrentUsage() >= voucher.getMaxUsage()) {
                return ResponseEntity.badRequest().body(
                        new ApiResponse(false, "Voucher đã hết lượt sử dụng")
                );
            }

            double totalPrice =request.getTotalPrice();
            // 4. Kiểm tra giá trị đơn hàng tối thiểu
            if (totalPrice < voucher.getMinimumOrderValue().doubleValue()) {
                return ResponseEntity.badRequest().body(
                        new ApiResponse(false,
                                String.format("Đơn hàng phải có giá trị tối thiểu %s để áp dụng voucher",
                                        voucher.getMinimumOrderValue()))
                );
            }

            // 5. Tính toán giá trị giảm giá

            double discountAmount = ShareUtil.calculateDiscountAmount(voucher,totalPrice,request.getShippingFee());

            return ResponseEntity.ok(new ApplyVoucherResponse(true, "Áp dụng voucher thành công", discountAmount));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse(false, e.getMessage())
            );
        }
    }


}
