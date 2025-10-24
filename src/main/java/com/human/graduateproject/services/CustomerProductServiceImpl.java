package com.human.graduateproject.services;

import com.human.graduateproject.entity.Product;
import com.human.graduateproject.mapper.ProductMapper;
import com.human.graduateproject.repository.ProductRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerProductServiceImpl implements CustomerProductService{

    private final ProductRepository productRepository;

    public CustomerProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;

    }



    @Override
    public List<Product> getProductByCategoryId(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

//    @Override
//    public List<Product> getProductByCategoryId(Long categoryId, int limit) {
//        Pageable pageable = (Pageable) PageRequest.of(0,limit);
//        return productRepository.findProductsByCategoryId(categoryId,pageable);
//    }

    @Override
    public List<Product> getRandomProductByCategoryId(Long categoryId, int limit) {
        // Lấy 3 sản phẩm ngẫu nhiên

        return productRepository.findRandomProductsByCategoryId(categoryId, 3);
    }

    @Override
    public List<Product> get4NewProduct() {
        // Lấy 4sản phẩm mới nhất

        return productRepository.findTop4ByOrderByIdDesc();
    }

}
