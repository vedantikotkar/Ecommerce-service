package com.example.auth.repository;

import com.example.auth.entity.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartProductRepository extends JpaRepository<CartProduct, String> {
    List<CartProduct> findByUserId(String userId);
    void deleteByUserIdAndProductId(String userId, String productId);
}
