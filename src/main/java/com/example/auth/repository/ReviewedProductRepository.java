package com.example.auth.repository;

import com.example.auth.entity.ReviewedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewedProductRepository extends JpaRepository<ReviewedProduct, String> {
    List<ReviewedProduct> findByUserId(String userId);
    void deleteByUserIdAndProductId(String userId, String productId);
}