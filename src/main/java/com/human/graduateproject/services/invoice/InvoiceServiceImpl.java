package com.human.graduateproject.services.invoice;

import com.human.graduateproject.entity.Invoice;
import com.human.graduateproject.entity.Order;
import com.human.graduateproject.enums.PaymentStatus;
import com.human.graduateproject.repository.InvoiceRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class InvoiceServiceImpl implements InvoiceService{

    private final InvoiceRepository invoiceRepository;
    private final InvoiceNumberGenerator invoiceNumberGenerator;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, InvoiceNumberGenerator invoiceNumberGenerator) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceNumberGenerator = invoiceNumberGenerator;
    }

    @Transactional
    @Override
    public Invoice createInvoiceFromOrder(Order order) {
        if (invoiceRepository.existsByOrderId(order.getId())) {
            throw new IllegalStateException("Invoice already exists for this order");
        }

        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(invoiceNumberGenerator.generateInvoiceNumber(order.getId()));
        invoice.setIssueDate(LocalDateTime.now());
        invoice.setOrder(order);

        // Copy thông tin từ Order
        invoice.setCustomerName(order.getCustomerName());
        invoice.setCustomerEmail(order.getCustomerEmail());
        invoice.setCustomerPhone(order.getPhone());
        invoice.setShippingAddress(order.getAddress());

        // Thông tin thanh toán
        invoice.setSubtotal(order.getTotalPrice());
        invoice.setDiscountAmount(order.getDiscountAmount());
        invoice.setShippingFee(order.getShippingFee());
        invoice.setTaxAmount(BigDecimal.ZERO); // Có thể tính thuế nếu cần
        invoice.setTotalAmount(order.getFinalAmount());

        // Thông tin nhân viên
        invoice.setProcessingStaff(order.getProcessingStaff());
        invoice.setDeliveryStaff(order.getDeliveryStaff());

        // Phương thức thanh toán
        invoice.setPaymentMethod(order.getPayment() != null ?
                String.valueOf(order.getPayment().getPaymentMethod()) : "Unknown");

        // Nếu đơn hàng đã thanh toán
        if (order.getPayment() != null && order.getPayment().getStatus() == PaymentStatus.PAID) {
            invoice.setPaid(true);
            invoice.setPaidAt(order.getPayment().getPaymentDate());
            invoice.setPaymentReference(order.getPayment().getTransactionId());
        }

        return invoiceRepository.save(invoice);
    }

}
