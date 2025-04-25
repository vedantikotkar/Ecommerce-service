package com.example.auth.repository;

import com.example.auth.entity.ReviewedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import java.util.List;

@Repository
public interface ReviewedProductRepository extends JpaRepository<ReviewedProduct, String> {

    @Query("SELECT r FROM ReviewedProduct r WHERE r.userId = :userId")
    List<ReviewedProduct> findByUserId(@Param("userId") String userId);

    // Soft delete by userId and productId
    @Modifying
    @Transactional
    @Query("UPDATE ReviewedProduct r SET r.isDeleted = true WHERE r.userId = :userId AND r.productId = :productId")
    void softDeleteByUserIdAndProductId(@Param("userId") String userId, @Param("productId") String productId);
}
