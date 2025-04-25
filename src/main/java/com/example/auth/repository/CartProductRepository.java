package com.example.auth.repository;

import com.example.auth.entity.CartProduct;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartProductRepository extends JpaRepository<CartProduct, String> {
    @Query("SELECT c FROM CartProduct c WHERE c.userId = :userId")
    List<CartProduct> findByUserId(@Param("userId") String userId);

    @Modifying
    @Query("UPDATE CartProduct c SET c.isDeleted = true WHERE c.userId = :userId AND c.productId = :productId")
    void softDeleteByUserIdAndProductId(String userId, String productId);


    @Query("SELECT c FROM CartProduct c WHERE c.userId = :userId AND c.productId = :productId")
    Optional<CartProduct> findByUserIdAndProductId(@Param("userId") String userId, @Param("productId") String productId);


}
