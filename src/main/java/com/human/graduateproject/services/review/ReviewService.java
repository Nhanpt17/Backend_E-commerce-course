package com.human.graduateproject.services.review;

import com.human.graduateproject.entity.Review;
import com.human.graduateproject.payload.ReviewProjection;
import com.human.graduateproject.payload.ReviewRequeset;

import java.util.List;

public interface ReviewService {
    Review createReview(ReviewRequeset review);

    List<ReviewProjection> getProductReviews(Long productId);

    Double getProductAverageRating(Long productId);

    Long getProductReviewCount(Long productId);



    List<ReviewProjection> getTopReviewsByProductId(Long productId, int limit);
}
