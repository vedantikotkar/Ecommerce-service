package com.example.auth.repository;

import com.example.auth.entity.RatedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RatedProductRepository extends JpaRepository<RatedProduct, String> {
    List<RatedProduct> findByUserId(String userId);
    void deleteByUserIdAndProductId(String userId, String productId);
}
