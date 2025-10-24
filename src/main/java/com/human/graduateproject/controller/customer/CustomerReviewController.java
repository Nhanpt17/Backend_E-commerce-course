package com.human.graduateproject.controller.customer;


import com.human.graduateproject.entity.Review;

import com.human.graduateproject.payload.ReviewProjection;
import com.human.graduateproject.payload.ReviewRequeset;
import com.human.graduateproject.services.review.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/customer/reviews")
public class CustomerReviewController {

    private final ReviewService reviewService;

    public CustomerReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody ReviewRequeset review) {
        
        // Validate rating (1-5)
        Review createdReview = reviewService.createReview( review);
        return ResponseEntity.ok(Collections.singletonMap("message","Review created successfully."));
    }


    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewProjection>> getProductReviews(@PathVariable Long productId) {

        List<ReviewProjection> reviewResponses = reviewService.getProductReviews(productId);
        return ResponseEntity.ok(reviewResponses);
    }


    @GetMapping("/limit-product")
    public ResponseEntity<List<ReviewProjection>> getTopReviewsByProductId(@RequestParam Long productId,@RequestParam int limit) {


        List<ReviewProjection> reviewResponses = reviewService.getTopReviewsByProductId(productId, limit);
        return ResponseEntity.ok(reviewResponses);
    }

    @GetMapping("/product/{productId}/stats")
    public ResponseEntity<Map<String, Object>> getProductReviewStats(@PathVariable Long productId) {
        Double averageRating = reviewService.getProductAverageRating(productId);
        Long reviewCount = reviewService.getProductReviewCount(productId);

        Map<String, Object> stats = new HashMap<>();
        stats.put("averageRating", averageRating != null ? averageRating : 0);
        stats.put("reviewCount", reviewCount);

        return ResponseEntity.ok(stats);
    }

}
