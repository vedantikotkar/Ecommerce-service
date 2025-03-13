package com.example.auth.repository;

import com.example.auth.entity.LikedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikedProductRepository extends JpaRepository<LikedProduct,String> {

    List<LikedProduct> findByUserId(String userId);

    void deleteByUserIdAndProductId(String userId, String productId);
}
