package com.human.graduateproject.services;

import com.human.graduateproject.dto.ProductDto;
import com.human.graduateproject.dto.ProductWithCategoryDto;
import com.human.graduateproject.entity.Category;
import com.human.graduateproject.entity.Product;
import com.human.graduateproject.mapper.ProductMapper;
import com.human.graduateproject.repository.CategoryRepository;
import com.human.graduateproject.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminProductServiceImpl implements  AdminProductService{
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;


    public AdminProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
       // this.productMapper = productMapper;
        this.categoryRepository = categoryRepository;
    }


    @Override
    public Product addProduct(ProductDto product) {
        // Kiểm tra xem đã tồn tại sản phẩm trùng tên chưa (không phân biệt hoa thường)
        if (productRepository.existsByNameIgnoreCase(product.getName())) {
            throw new RuntimeException("Product with the same name already exists: " + product.getName());
        }

        Product product1 = ProductMapper.mapToProduct(product);
        Category category = categoryRepository.findById(product.getCategoryId()).orElseThrow(()->new EntityNotFoundException("Category not found"));
        product1.setCategory(category);
        // Lưu sản phẩm vào cơ sở dữ liệu

        // Trả về ProductDto đã lưu
        return productRepository.save(product1);
    }



    @Override
    public List<ProductWithCategoryDto> getAllProducts() {

        return productRepository.findAllProductDtos();
    }
    @Override
    public List<Product> getAllCustomerProducts() {

        return productRepository.findAll();
    }
    // soft delete
    @Override
    public boolean deleteProduct(Long id) {
        int updatedRows = productRepository.softDeleteById(id);
        if (updatedRows == 0) {
            throw new EntityNotFoundException("Product not found with id: " + id);
        }
        return true;
    }

    @Override
    public Product updateProduct(Long id, ProductDto productDto) {

        // 1. Check sản phẩm tồn tại
        if (!productRepository.existsById(id)) {
            throw new EntityNotFoundException("Product not found with id: " + id);
        }

        // 2. Check tên sản phẩm trùng với sản phẩm khác (loại trừ chính nó)
        Optional<Long> existingProduct = productRepository.findIdByNameIgnoreCase(productDto.getName());
        if (existingProduct.isPresent() && !existingProduct.get().equals(id)) {
            throw new IllegalArgumentException("Product name already exists: " + productDto.getName());
        }


        Product product = ProductMapper.mapToProduct(productDto);

        Category category = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(()->new EntityNotFoundException("Category not found"));
        product.setCategory(category);

        return productRepository.save(product);
    }

    @Override
    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(()->new EntityNotFoundException("Product not found with id:"+productId));
    }
}
