package com.example.auth.service;

import com.example.auth.entity.ReviewedProduct;
import com.example.auth.repository.ReviewedProductRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReviewedProductService {

    private final ReviewedProductRepository reviewedProductRepository;

    public ReviewedProductService(ReviewedProductRepository reviewedProductRepository) {
        this.reviewedProductRepository = reviewedProductRepository;
    }

    public ReviewedProduct reviewProduct(String userId, String productId, String review) {
        ReviewedProduct reviews = new ReviewedProduct();
        reviews.setUserId(userId);
        reviews.setProductId(productId);
        reviews.setReview(review);
        return reviewedProductRepository.save(reviews);
    }


    // Get all reviewed products by a user
    public List<ReviewedProduct> getReviewedProducts(String userId) {
        return reviewedProductRepository.findByUserId(userId);
    }

    // Unreview a product
    public void unreviewProduct(String userId, String productId) {
        reviewedProductRepository.softDeleteByUserIdAndProductId(userId, productId);
    }

    // Get all reviewed products
    public List<ReviewedProduct> getAllReviewedProducts() {
        return reviewedProductRepository.findAll();
    }
}
