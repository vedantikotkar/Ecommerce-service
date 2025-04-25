package com.example.auth.service;

import com.example.auth.entity.Wishlist;
import com.example.auth.repository.WishlistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;

    public WishlistService(WishlistRepository wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
    }

    public List<Wishlist> getUserWishlist(String userId) {
        return wishlistRepository.findByUserId(userId);
    }

    public Wishlist addToWishlist(Wishlist wishlistItem) {
        return wishlistRepository.save(wishlistItem);
    }

//    @Transactional
//    public void removeFromWishlistByProductId(String productId) {
//        wishlistRepository.deleteByProductId(productId);
//    }

}
