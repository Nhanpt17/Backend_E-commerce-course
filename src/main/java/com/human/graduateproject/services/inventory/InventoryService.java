package com.human.graduateproject.services.inventory;

import com.human.graduateproject.dto.InventoryCheckResponse;
import com.human.graduateproject.entity.Order;
import org.springframework.transaction.annotation.Transactional;

public interface InventoryService {


    InventoryCheckResponse checkInventory(Order order);

    @Transactional
    void deductInventory(Order order);
}
