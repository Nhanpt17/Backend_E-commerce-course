package com.human.graduateproject.repository;

import com.human.graduateproject.dto.ProductWithCategoryDto;
import com.human.graduateproject.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    boolean existsByCategoryId(Long id);
    List<Product> findByCategoryId(Long categoryId);

    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId")
    List<Product> findProductsByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

    @Query(value = "SELECT * FROM product WHERE category_id = :categoryId AND enable = true ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<Product> findRandomProductsByCategoryId(@Param("categoryId") Long categoryId, @Param("limit") int limit);

    boolean existsByNameIgnoreCase(String name);

    @Query("SELECT p.id FROM Product p WHERE LOWER(p.name) = LOWER(:name)")
    Optional<Long> findIdByNameIgnoreCase(@Param("name") String name);

    @Transactional
    @Modifying
    @Query("UPDATE Product p SET p.enable = false WHERE p.id = :id")
    int softDeleteById(@Param("id") Long id);

    List<Product> findTop4ByOrderByIdDesc();

    @Query("SELECT p.id as id, p.name as name, p.price as price, p.stock as stock, " +
            "p.description as description, p.imgUrl as imgUrl, " +
            "c.id as categoryId, c.name as categoryName " +
            "FROM Product p JOIN p.category c " +  // Bỏ WHERE
            "ORDER BY p.id DESC")
    List<ProductWithCategoryDto> findAllProductDtos();



}
