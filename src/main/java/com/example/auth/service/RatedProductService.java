package com.example.auth.service;

import com.example.auth.entity.RatedProduct;
import com.example.auth.repository.RatedProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatedProductService {

    private final RatedProductRepository ratedProductRepository;

    public RatedProductService(RatedProductRepository ratedProductRepository) {
        this.ratedProductRepository = ratedProductRepository;
    }

    public RatedProduct rateProduct(String userId, String productId, int rating) {
        RatedProduct ratedProduct = new RatedProduct();
        ratedProduct.setUserId(userId);
        ratedProduct.setProductId(productId);
        ratedProduct.setRating(rating);
        return ratedProductRepository.save(ratedProduct);
    }

    // Get all rated products by a user
    public List<RatedProduct> getRatedProducts(String userId) {
        return ratedProductRepository.findByUserId(userId);
    }

    // Unrate a product
    public void unrateProduct(String userId, String productId) {
        ratedProductRepository.softDeleteByUserIdAndProductId(userId, productId);
    }

    // Get all rated products
    public List<RatedProduct> getAllRatedProducts() {
        return ratedProductRepository.findAll();
    }
}
