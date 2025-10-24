package com.human.graduateproject.services.invoice;

import com.human.graduateproject.entity.Invoice;
import com.human.graduateproject.entity.Order;
import jakarta.transaction.Transactional;

public interface InvoiceService {
    @Transactional
    Invoice createInvoiceFromOrder(Order order);
}
