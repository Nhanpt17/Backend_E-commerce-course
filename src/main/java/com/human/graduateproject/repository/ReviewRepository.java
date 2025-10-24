package com.human.graduateproject.repository;

import com.human.graduateproject.entity.Review;
import com.human.graduateproject.payload.ReviewProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {





    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.id = :productId")
    Double getAverageRatingByProductId(@Param("productId") Long productId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.product.id = :productId")
    Long getReviewCountByProductId(@Param("productId") Long productId);

    Optional<Review> findByProductIdAndUserId(Long productId, Long userId);

    @Query("""
    SELECT\s
        r.id AS id,
        r.comment AS comment,
        r.rating AS rating,
        p.name AS productName,
        u.name AS username
    FROM Review r
    JOIN r.product p
    JOIN r.user u
    WHERE p.id = :productId
""")
    List<ReviewProjection> findAllProjectedByProductId(@Param("productId") Long productId);

    @Query("""
    SELECT\s
        r.id AS id,
        r.comment AS comment,
        r.rating AS rating,
        p.name AS productName,
        u.name AS username
    FROM Review r
    JOIN r.product p
    JOIN r.user u
    WHERE p.id = :productId
    ORDER BY r.createdAt DESC
""")
    List<ReviewProjection> findTopProjectedByProductId(@Param("productId") Long productId, Pageable pageable);

}
