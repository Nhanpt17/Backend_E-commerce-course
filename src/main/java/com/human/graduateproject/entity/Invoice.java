package com.human.graduateproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String invoiceNumber; // Số hóa đơn tự động tạo

    @Column(nullable = false)
    private LocalDateTime issueDate; // Ngày xuất hóa đơn

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order; // Tham chiếu đến đơn hàng

    // Thông tin khách hàng (lưu trữ để đảm bảo tính toàn vẹn khi dữ liệu khách hàng thay đổi)
    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String customerEmail;

    @Column(nullable = false)
    private String customerPhone;

    @Column(nullable = false)
    private String shippingAddress;

    // Thông tin thanh toán
    @Column(nullable = false)
    private BigDecimal subtotal; // Tổng tiền hàng


    private BigDecimal discountAmount; // Tiền giảm giá


    private BigDecimal shippingFee; // Phí vận chuyển


    private BigDecimal taxAmount; // Thuế (nếu có)

    @Column(nullable = false)
    private BigDecimal totalAmount; // Tổng thanh toán

    // Thông tin nhân viên
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "processing_staff_id")
    private Users processingStaff; // Nhân viên xử lý đơn

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_staff_id")
    private Users deliveryStaff; // Nhân viên giao hàng

    @Column(nullable = false)
    private String paymentMethod; // Phương thức thanh toán

    @Column(length = 1000)
    private String notes; // Ghi chú hóa đơn

    // Các trạng thái hóa đơn
    @Column(nullable = false)
    private boolean isPaid = false; // Đã thanh toán chưa

    private LocalDateTime paidAt; // Thời điểm thanh toán

    @Column(length = 50)
    private String paymentReference; // Số tham chiếu thanh toán
}
