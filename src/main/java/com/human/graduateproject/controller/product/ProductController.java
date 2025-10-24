package com.human.graduateproject.controller.product;

import com.human.graduateproject.dto.CategoryDto;
import com.human.graduateproject.dto.ProductDto;
import com.human.graduateproject.entity.Product;
import com.human.graduateproject.mapper.ProductMapper;
import com.human.graduateproject.services.AdminProductService;
import com.human.graduateproject.services.CategoryService;
import com.human.graduateproject.services.CustomerProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final AdminProductService adminProductService;
    private final CategoryService categoryService;
    private final CustomerProductService customerProductService;

    public ProductController(AdminProductService adminProductService, CategoryService categoryService, CustomerProductService customerProductService) {
        this.adminProductService = adminProductService;
        this.categoryService = categoryService;
        this.customerProductService = customerProductService;
    }

    @GetMapping()
    public ResponseEntity<List<ProductDto>> getAllProduct(){
        List<Product> products = adminProductService.getAllCustomerProducts();
        return ResponseEntity.ok(products.stream().map(ProductMapper::mapToProductDto).collect(Collectors.toList()));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long productId){
        Product product = adminProductService.getProductById(productId);
        return ResponseEntity.ok(ProductMapper.mapToProductDto(product));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> getAllCategories(){
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Product>> getProductByCategoryId(@PathVariable Long categoryId){
        List<Product> productDtoList = customerProductService.getProductByCategoryId(categoryId);
        return ResponseEntity.ok(productDtoList);
    }

    @GetMapping("/category/limit/{categoryId}")
    public ResponseEntity<List<ProductDto>> getLimitProductByCategoryId(@PathVariable Long categoryId, @RequestParam(defaultValue = "3") int limit){
        List<Product> productDtoList = customerProductService.getRandomProductByCategoryId(categoryId,limit);
        return ResponseEntity.ok(productDtoList.stream().map(ProductMapper::mapToProductDto).collect(Collectors.toList()));
    }

    @GetMapping("/top/four")
    public ResponseEntity<List<ProductDto>> getTop4NewProduct(){
        List<Product> productDtoList = customerProductService.get4NewProduct();
        return ResponseEntity.ok(productDtoList.stream().map(ProductMapper::mapToProductDto).collect(Collectors.toList()));
    }


}
