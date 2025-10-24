package com.human.graduateproject.services.voucher;

import com.human.graduateproject.entity.CustomerVoucher;
import com.human.graduateproject.entity.Point;
import com.human.graduateproject.entity.PointTransaction;
import com.human.graduateproject.entity.Voucher;
import com.human.graduateproject.enums.TransactionType;
import com.human.graduateproject.repository.CustomerVoucherRepository;
import com.human.graduateproject.repository.PointRepository;
import com.human.graduateproject.repository.PointTransactionRepository;
import com.human.graduateproject.repository.VoucherRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VoucherServiceImpl implements VoucherService{

    private final VoucherRepository voucherRepository;
    private final CustomerVoucherRepository customerVoucherRepository;
    private final PointRepository pointRepository;
    private final PointTransactionRepository pointTransactionRepository;

    public VoucherServiceImpl(VoucherRepository voucherRepository, CustomerVoucherRepository customerVoucherRepository, PointRepository pointRepository, PointTransactionRepository pointTransactionRepository) {
        this.voucherRepository = voucherRepository;
        this.customerVoucherRepository = customerVoucherRepository;
        this.pointRepository = pointRepository;
        this.pointTransactionRepository = pointTransactionRepository;
    }

    @Override
    public Voucher createVoucher(Voucher voucher) {

        if(voucher ==null){
            throw new RuntimeException("Voucher cannot be null");
        }
        Optional<Voucher> voucher1 = voucherRepository.findByCode(voucher.getCode());
        if(voucher1.isPresent()){
            throw  new RuntimeException("Code's voucher is exist");
        }

        return voucherRepository.save(voucher);

    }

    @Override
    public Voucher updateVoucher(Long voucherId, Voucher voucherDetails) {
        Voucher existingVoucher = voucherRepository.findById(voucherId)
                .orElseThrow(()->new EntityNotFoundException("Voucher not found with id: "+voucherId));

        // Chỉ check nếu code thay đổi
        String newCode = voucherDetails.getCode();
        if (!existingVoucher.getCode().equals(newCode)) {
            voucherRepository.findByCode(newCode).ifPresent(voucher -> {
                throw new RuntimeException("Another voucher with the same code already exists.");
            });
            existingVoucher.setCode(newCode); // set ngay luôn
        }


        existingVoucher.setDescription(voucherDetails.getDescription());
        existingVoucher.setDiscountValue(voucherDetails.getDiscountValue());
        existingVoucher.setDiscountType(voucherDetails.getDiscountType());
        existingVoucher.setMinimumOrderValue(voucherDetails.getMinimumOrderValue());
        existingVoucher.setStartDate(voucherDetails.getStartDate());
        existingVoucher.setEndDate(voucherDetails.getEndDate());
        existingVoucher.setMaxUsage(voucherDetails.getMaxUsage());
        existingVoucher.setPublic(voucherDetails.isPublic());
        existingVoucher.setPointsRequired(voucherDetails.getPointsRequired());

         return  voucherRepository.save(existingVoucher);

    }

    @Override
    public void deleteVoucher(Long voucherId) {

        if (voucherRepository.softDeleteVoucher(voucherId) ==0){
            throw new EntityNotFoundException("Voucher not found");
        }

    }

    @Override
    public List<Voucher> getAllVouchers() {

        return voucherRepository.findAll();
    }

    @Override
    public List<Voucher> getPublicVouchers(){

        return voucherRepository.findByIsPublicTrueAndEndDateAfter( LocalDateTime.now());

    }

    @Override
    public Voucher redeemVoucher(Long customerId, Long voucherId){

        Voucher voucher = voucherRepository.findById(voucherId)
                .orElseThrow(()-> new EntityNotFoundException("Voucher not found"));

        LocalDateTime now = LocalDateTime.now();


        if(voucher.getEndDate().isBefore(now)){
            throw new RuntimeException("Voucher is not available");
        }

        if (voucher.getMaxUsage() !=null && voucher.getCurrentUsage() >= voucher.getMaxUsage()){
            throw  new RuntimeException("Voucher has reached its usage limit");
        }

            Optional<CustomerVoucher> optionalCV  = customerVoucherRepository.findByCustomerIdAndVoucher_Id(customerId,voucherId);

            if (optionalCV .isPresent()){
                CustomerVoucher customerVoucher = optionalCV.get();

                if (!customerVoucher.isUsed()){
                    throw new RuntimeException("You already have this voucher");
                }

                customerVoucher.setUsed(false);
                customerVoucher.setCreatedAt(now);

                customerVoucherRepository.save(customerVoucher);

            }else {
                CustomerVoucher customerVoucher = new CustomerVoucher();
                customerVoucher.setCustomerId(customerId);
                customerVoucher.setVoucher(voucher);
                customerVoucher.setCreatedAt(now);
                customerVoucher.setUsed(false);
                customerVoucherRepository.save(customerVoucher);
            }

        voucher.setCurrentUsage(voucher.getCurrentUsage()+1);
        voucherRepository.save(voucher);

        return  voucher;

    }

    @Override
    public Point getCustomerPoints(Long customerId){
        return pointRepository.findByCustomerId(customerId)
                .orElseGet(()->{
                    Point newPoint = new Point();
                    newPoint.setCustomerId(customerId);
                    return pointRepository.save(newPoint);
                });
    }

    @Transactional
    @Override
    public Point redeemPointsForVoucher(Long customerId, Long voucherId, Integer pointsRequired){
        Point point = getCustomerPoints(customerId);
        if(point.getBalance() < pointsRequired){
            throw new RuntimeException("Not enough points");
        }

        Voucher voucher = redeemVoucher(customerId, voucherId);

        point.setBalance(point.getBalance() - pointsRequired);
        pointRepository.save(point);

        PointTransaction transaction = new PointTransaction();
        transaction.setCustomerId(customerId);
        transaction.setPointsChange(-pointsRequired);
        transaction.setTransactionType(TransactionType.REDEEM);
        transaction.setReferenceId(voucher.getId());
        transaction.setReferenceType("VOUCHER");
        pointTransactionRepository.save(transaction);

        return  point;
    }






    @Override
    public List<CustomerVoucher> getCustomerVouchers(Long customerId) {
        return customerVoucherRepository
                .findByCustomerIdAndUsedFalse(customerId);
    }

    @Override
    public void markCustomerVoucherAsUsed(Long customerId, Long voucherId){
        int updated = customerVoucherRepository.markAsUsed(customerId, voucherId, LocalDateTime.now());

        if (updated == 0) {
            throw new EntityNotFoundException("CustomerVoucher not found");
        }
    }

    @Override
    public void incrementVoucherUsage(String voucherCode){
        int updatedRows = voucherRepository.incrementUsageByCode(voucherCode);
        if (updatedRows == 0) {
            throw new EntityNotFoundException("Voucher not found with code: " + voucherCode);
        }
    }


}
