package com.human.graduateproject.controller.admin;

import com.human.graduateproject.dto.ProductDto;
import com.human.graduateproject.dto.ProductWithCategoryDto;
import com.human.graduateproject.entity.Product;
import com.human.graduateproject.mapper.ProductMapper;
import com.human.graduateproject.services.AdminProductService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminProductController {

    private final AdminProductService adminProductService;


    public AdminProductController(AdminProductService adminProductService) {
        this.adminProductService = adminProductService;
    }



    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestBody ProductDto productDto) {

        // Save sản phẩm vào DB
        adminProductService.addProduct(productDto);
        return ResponseEntity.ok(true) ;
    }


    @PutMapping("/update-product/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id,@RequestBody ProductDto productDto){


        try{
            Product savedProduct =  adminProductService.updateProduct(id, productDto);

            return ResponseEntity.ok(ProductMapper.mapToProductDto(savedProduct));
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message",e.getMessage()));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message",e.getMessage()));
        }


    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductWithCategoryDto>> getAllProduct(){
        List<ProductWithCategoryDto> products = adminProductService.getAllProducts();

        return ResponseEntity.ok(products);

    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long productId){
        Product product = adminProductService.getProductById(productId);

        return ResponseEntity.ok(ProductMapper.mapToProductDto(product));
    }


    @DeleteMapping("product/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId){
        boolean deleted = adminProductService.deleteProduct(productId);

        if (deleted)
            return ResponseEntity.noContent().build();

        return ResponseEntity.notFound().build();
    }

}
