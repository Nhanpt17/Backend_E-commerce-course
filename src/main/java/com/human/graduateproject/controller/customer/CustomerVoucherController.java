package com.human.graduateproject.controller.customer;

import com.human.graduateproject.entity.CustomerVoucher;
import com.human.graduateproject.entity.Point;
import com.human.graduateproject.entity.Voucher;
import com.human.graduateproject.payload.ApiResponse;
import com.human.graduateproject.payload.ApplyCustomerVoucherRequest;
import com.human.graduateproject.payload.ApplyVoucherResponse;
import com.human.graduateproject.repository.CustomerVoucherRepository;
import com.human.graduateproject.services.voucher.VoucherService;
import com.human.graduateproject.util.shared.ShareUtil;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customer/vouchers")
public class CustomerVoucherController {
    private final VoucherService voucherService;
    private final CustomerVoucherRepository customerVoucherRepository;

    public CustomerVoucherController(VoucherService voucherService, CustomerVoucherRepository customerVoucherRepository) {
        this.voucherService = voucherService;
        this.customerVoucherRepository = customerVoucherRepository;
    }

    @GetMapping("/public")
    public ResponseEntity<List<Voucher>> getPublicVoucher(){
        return ResponseEntity.ok(voucherService.getPublicVouchers());
    }


    @GetMapping("/my-vouchers")
    public ResponseEntity<List<Voucher>> getCustomerVouchers(@RequestParam Long customerId){
        List<CustomerVoucher> customerVouchers = voucherService.getCustomerVouchers(customerId);
        List<Voucher> vouchers = customerVouchers.stream()
                .map(CustomerVoucher::getVoucher)
                .collect(Collectors.toList());

        return ResponseEntity.ok(vouchers);
    }



    @PostMapping("/redeem-with-points")
    public ResponseEntity<Point> redeemWithPoints(
            @RequestParam Long customerId,
            @RequestParam Long voucherId,
            @RequestParam Integer pointsRequired){
        return ResponseEntity.ok(voucherService.redeemPointsForVoucher(customerId,voucherId,pointsRequired));
    }

    @GetMapping("/points")
    public ResponseEntity<Point> getCustomerPoints(@RequestParam Long customerId){
        return ResponseEntity.ok(voucherService.getCustomerPoints(customerId));
    }

    @PostMapping("/customer-voucher-apply")
    public ResponseEntity<?> applyCustomerVoucher(@RequestBody ApplyCustomerVoucherRequest request) {
        try {

            // 1. Kiểm tra customer voucher có tồn tại và chưa sử dụng
            CustomerVoucher customerVoucher = customerVoucherRepository
                    .findByCustomerIdAndVoucher_Id(request.getCustomerId(), request.getVoucherId())
                    .orElseThrow(() -> new RuntimeException("Voucher không tồn tại trong tài khoản của bạn"));

            System.out.println("customer voucher: "+customerVoucher);

            if (customerVoucher.isUsed()) {
                return ResponseEntity.badRequest().body(
                        new ApiResponse(false, "Voucher đã được sử dụng")
                );
            }

            Voucher voucher = customerVoucher.getVoucher();

            // 2. Kiểm tra thời gian hiệu lực
            LocalDateTime now = LocalDateTime.now();
            if (now.isBefore(voucher.getStartDate()) || now.isAfter(voucher.getEndDate())) {
                return ResponseEntity.badRequest().body(
                        new ApiResponse(false, "Voucher đã hết hạn hoặc chưa đến thời gian sử dụng")
                );
            }

            // 3. Kiểm tra giá trị đơn hàng tối thiểu
            if (request.getTotalPrice() < voucher.getMinimumOrderValue().doubleValue()) {
                return ResponseEntity.badRequest().body(
                        new ApiResponse(false,
                                String.format("Đơn hàng phải có giá trị tối thiểu %s để áp dụng voucher",
                                        voucher.getMinimumOrderValue()))
                );
            }

            // 4. Tính toán giá trị giảm giá
            double discountAmount = ShareUtil.calculateDiscountAmount(voucher, request.getTotalPrice(), request.getShippingFee());

            return ResponseEntity.ok(new ApplyVoucherResponse(true, "Áp dụng voucher thành công", discountAmount));
        } catch (Exception e) {
            System.out.println("có lỗi: " + e.getMessage());

            return ResponseEntity.badRequest().body(
                    new ApiResponse(false, e.getMessage())
            );
        }
    }

    @GetMapping("/customer-vouchers/{customerId}/available")
    public ResponseEntity<?> getAvailableCustomerVouchers(@PathVariable Long customerId) {
        try {
            List<CustomerVoucher> customerVouchers = customerVoucherRepository
                    .findByCustomerIdAndUsedFalse(customerId);

           
            // Lọc các voucher còn hiệu lực
            LocalDateTime now = LocalDateTime.now();
            List<Voucher> availableVouchers = customerVouchers.stream()
                    .filter(cv ->
                            !cv.isUsed() &&
                                    now.isAfter(cv.getVoucher().getStartDate()) &&
                                    now.isBefore(cv.getVoucher().getEndDate()))
                    .map(CustomerVoucher::getVoucher) // chuyển sang Voucher
                    .collect(Collectors.toList());

            return ResponseEntity.ok(availableVouchers);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse(false, "Có lỗi xảy ra khi lấy danh sách voucher")
            );
        }
    }


    @PostMapping("/mark-used")
    public ResponseEntity<CustomerVoucher> markCustomerVoucherAsUsed(@RequestParam Long customerId, @RequestParam Long voucherId ){
       voucherService.markCustomerVoucherAsUsed(customerId,voucherId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/increment-voucher/{voucherCode}")
    public ResponseEntity<Voucher> incrementVoucherUsage(@PathVariable String voucherCode){
       voucherService.incrementVoucherUsage(voucherCode);
        return ResponseEntity.ok().build();
    }


}
