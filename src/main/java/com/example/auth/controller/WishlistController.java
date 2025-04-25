package com.example.auth.controller;

import com.example.auth.entity.Wishlist;
import com.example.auth.service.WishlistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Wishlist>> getWishlist(@PathVariable String userId) {
        return ResponseEntity.ok(wishlistService.getUserWishlist(userId));
    }

    @PostMapping("/add")
    public ResponseEntity<Wishlist> addToWishlist(@RequestBody Wishlist wishlistItem) {
        return ResponseEntity.ok(wishlistService.addToWishlist(wishlistItem));
    }


}
