package com.example.auth.service;

import com.example.auth.entity.LikedProduct;
import com.example.auth.repository.LikedProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikedProductService {

    private final LikedProductRepository likedProductRepository;

    public LikedProductService(LikedProductRepository likedProductRepository) {
        this.likedProductRepository = likedProductRepository;
    }

    public LikedProduct likeProduct(String userId, String productId) {
        LikedProduct likedProduct = new LikedProduct();
        likedProduct.setUserId(userId);
        likedProduct.setProductId(productId);
        return likedProductRepository.save(likedProduct);
    }

    public List<LikedProduct> getLikedProducts(String userId) {
        return likedProductRepository.findByUserId(userId);
    }

    public void unlikeProduct(String userId, String productId) {
        likedProductRepository.deleteByUserIdAndProductId(userId, productId);
    }

    public List<LikedProduct> getAllLikedProducts() {
        return likedProductRepository.findAll();
    }
}
