package com.human.graduateproject.services;

import com.human.graduateproject.dto.ProductDto;
import com.human.graduateproject.dto.ProductWithCategoryDto;
import com.human.graduateproject.entity.Product;

import java.util.List;

public interface AdminProductService {
    Product addProduct(ProductDto product);
    List<ProductWithCategoryDto> getAllProducts();

    List<Product> getAllCustomerProducts();

    boolean deleteProduct(Long id);
    Product updateProduct(Long id,ProductDto productDto);

    Product getProductById(Long productId);
}
