package com.human.graduateproject.mapper;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.human.graduateproject.dto.ProductDto;
import com.human.graduateproject.entity.Category;
import com.human.graduateproject.entity.Product;
import com.human.graduateproject.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Map;


public class ProductMapper {





    public  static   ProductDto mapToProductDto(Product product){
        if (product == null) {
            return null;
        }

        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStock(),
                product.getDescription(),
                product.getImgUrl(),
                product.getCategory().getId()
        );
    }


    public static Product mapToProduct(ProductDto productDto){

        Product product = new Product();
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setStock(productDto.getStock());
        product.setDescription(productDto.getDescription());
        product.setImgUrl(productDto.getImgUrl());

        return product;
    }


}
