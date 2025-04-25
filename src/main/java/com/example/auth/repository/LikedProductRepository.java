package com.example.auth.repository;

import com.example.auth.entity.LikedProduct;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikedProductRepository extends JpaRepository<LikedProduct,String> {
    @Query("SELECT lp FROM LikedProduct lp WHERE lp.userId = :userId")
    List<LikedProduct> findByUserId(@Param("userId") String userId);

    @Modifying
    @Transactional
    @Query("UPDATE LikedProduct lp SET lp.isDeleted = true WHERE lp.productId = :productId")
    void softDeleteByProductId(@Param("productId") String productId);

    // Soft delete by userId and productId
    @Modifying
    @Transactional
    @Query("UPDATE LikedProduct lp SET lp.isDeleted = true WHERE lp.userId = :userId AND lp.productId = :productId")
    void softDeleteByUserIdAndProductId(@Param("userId") String userId, @Param("productId") String productId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Product p WHERE p.user.id = :userId AND p.id = :productId")
    void deleteByUserIdAndProductId(@Param("userId") String userId, @Param("productId") String productId);

}