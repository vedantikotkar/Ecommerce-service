package com.example.auth.controller;

import com.example.auth.entity.LikedProduct;
import com.example.auth.service.LikedProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/like")
public class LikedProductController {

    private final LikedProductService likedProductService;

    public LikedProductController(LikedProductService likedProductService) {
        this.likedProductService = likedProductService;
    }

    @PostMapping("/liked")
    public ResponseEntity<LikedProduct> likeProduct(@RequestParam String userId, @RequestParam String productId) {
        return ResponseEntity.ok(likedProductService.likeProduct(userId, productId));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<LikedProduct>> getLikedProducts(@PathVariable String userId) {
        return ResponseEntity.ok(likedProductService.getLikedProducts(userId));
    }

    @DeleteMapping("/unlike")
    public ResponseEntity<String> unlikeProduct(@RequestParam String userId, @RequestParam String productId) {
        likedProductService.unlikeProduct(userId, productId);
        return ResponseEntity.ok(" Product unliked successfully!");
    }

    @GetMapping("/")
    public ResponseEntity<List<LikedProduct>> getAllLikedProducts() {
        return ResponseEntity.ok(likedProductService.getAllLikedProducts());
    }
}
