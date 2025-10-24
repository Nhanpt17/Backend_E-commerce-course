package com.human.graduateproject.services.inventory;

import com.human.graduateproject.dto.InventoryCheckResponse;
import com.human.graduateproject.entity.Order;
import com.human.graduateproject.entity.OrderItem;
import com.human.graduateproject.entity.Product;
import com.human.graduateproject.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class InventoryServiceImpl implements  InventoryService{

    private final ProductRepository productRepository;

    public InventoryServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public InventoryCheckResponse checkInventory(Order order) {
        List<OrderItem> items = order.getOrderItems();
        List<String> outOfStockItems = new ArrayList<>();
        boolean isAvailable = true;

        for (OrderItem item : items) {
            Product product = item.getProduct();
            if (product.getStock() < item.getQuantity()) {
                isAvailable = false;
                outOfStockItems.add(product.getName() +
                        " (Cần: " + item.getQuantity() +
                        ", Có: " + product.getStock() + ")");
            }
        }

        String message = isAvailable ?
                "Tất cả sản phẩm có đủ số lượng trong kho" :
                "Một số sản phẩm không đủ số lượng: " + String.join(", ", outOfStockItems);

        return new InventoryCheckResponse(isAvailable, message);
    }



    @Override
    public void deductInventory(Order order) {
        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            Long newQuantity =  product.getStock() - item.getQuantity();
            if (newQuantity < 0) {
                throw new RuntimeException("Sản phẩm " + product.getName() + " không đủ số lượng");
            }
            product.setStock(newQuantity);
            productRepository.save(product);
        }
    }



}
