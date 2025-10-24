package com.human.graduateproject.services;

import com.human.graduateproject.entity.Product;

import java.util.List;

public interface CustomerProductService {
    List<Product> getProductByCategoryId(Long categoryId);


//    List<Product> getProductByCategoryId(Long categoryId, int limit);

    List<Product> getRandomProductByCategoryId(Long categoryId, int limit);

    List<Product> get4NewProduct();
}
