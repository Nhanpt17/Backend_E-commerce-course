package com.human.graduateproject.services.review;

import com.human.graduateproject.entity.Product;
import com.human.graduateproject.entity.Review;
import com.human.graduateproject.entity.Users;
import com.human.graduateproject.payload.ReviewProjection;
import com.human.graduateproject.payload.ReviewRequeset;
import com.human.graduateproject.repository.ProductRepository;
import com.human.graduateproject.repository.ReviewRepository;
import com.human.graduateproject.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Review createReview(ReviewRequeset review) {
        Product product = productRepository.findById(review.getProductId()).orElseThrow(()-> new EntityNotFoundException("Product not found"));
        Users user = userRepository.findById(review.getUserId()).orElseThrow(()-> new EntityNotFoundException("User not found"));
        Review savedReview = new Review();
        savedReview.setUser(user);
        savedReview.setProduct(product);
        savedReview.setRating(review.getRating());
        savedReview.setComment(review.getComment());
        return reviewRepository.save(savedReview);
    }


    @Override
    public List<ReviewProjection> getProductReviews(Long productId) {
        return reviewRepository.findAllProjectedByProductId(productId);
    }

    @Override
    public Double getProductAverageRating(Long productId) {
        return reviewRepository.getAverageRatingByProductId(productId);
    }

    @Override
    public Long getProductReviewCount(Long productId) {
        return reviewRepository.getReviewCountByProductId(productId);
    }




    @Override
    public List<ReviewProjection> getTopReviewsByProductId(Long productId, int limit) {
        return reviewRepository.findTopProjectedByProductId(productId, PageRequest.of(0, limit));
    }

}
