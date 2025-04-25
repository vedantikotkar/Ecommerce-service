package com.example.auth.repository;

import com.example.auth.entity.RatedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import java.util.List;

@Repository
public interface RatedProductRepository extends JpaRepository<RatedProduct, String> {

    @Query("SELECT r FROM RatedProduct r WHERE r.userId = :userId")
    List<RatedProduct> findByUserId(@Param("userId") String userId);

    @Modifying
    @Transactional
    @Query("UPDATE RatedProduct r SET r.isDeleted = true WHERE r.userId = :userId AND r.productId = :productId")
    void softDeleteByUserIdAndProductId(@Param("userId") String userId, @Param("productId") String productId);
}
