package com.human.graduateproject.controller.admin;

import com.human.graduateproject.entity.Voucher;
import com.human.graduateproject.services.voucher.VoucherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/vouchers")
public class AdminVoucherController {

    private  final VoucherService voucherService;


    public AdminVoucherController(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    @PostMapping
    public ResponseEntity<Voucher> createVoucher(@RequestBody Voucher voucher){
        Voucher createdVoucher = voucherService.createVoucher(voucher);
        return new ResponseEntity<>(createdVoucher, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Voucher> updateVoucher(@PathVariable Long id, @RequestBody Voucher voucherDetails){

        Voucher updatedVoucher = voucherService.updateVoucher(id,voucherDetails);

        return ResponseEntity.ok(updatedVoucher);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVoucher(@PathVariable Long id){
        voucherService.deleteVoucher(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Voucher>> getAllVouchers(){
        List<Voucher> voucherList = voucherService.getAllVouchers();
        return ResponseEntity.ok(voucherList);
    }

}
