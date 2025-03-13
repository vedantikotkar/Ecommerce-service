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

    // Review a product
    public ReviewedProduct reviewProduct(String userId, String productId) {
        ReviewedProduct reviewedProduct = new ReviewedProduct();
        reviewedProduct.setUserId(userId);
        reviewedProduct.setProductId(productId);
        return reviewedProductRepository.save(reviewedProduct);
    }

    // Get all reviewed products by a user
    public List<ReviewedProduct> getReviewedProducts(String userId) {
        return reviewedProductRepository.findByUserId(userId);
    }

    // Unreview a product
    public void unreviewProduct(String userId, String productId) {
        reviewedProductRepository.deleteByUserIdAndProductId(userId, productId);
    }

    // Get all reviewed products
    public List<ReviewedProduct> getAllReviewedProducts() {
        return reviewedProductRepository.findAll();
    }
}
