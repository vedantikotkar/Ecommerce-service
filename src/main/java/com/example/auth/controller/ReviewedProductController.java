package com.example.auth.controller;

import com.example.auth.entity.ReviewedProduct;
import com.example.auth.service.ReviewedProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviewed-products")
public class ReviewedProductController {

    private final ReviewedProductService reviewedProductService;

    public ReviewedProductController(ReviewedProductService reviewedProductService) {
        this.reviewedProductService = reviewedProductService;
    }

    @PostMapping("/review")
    public ResponseEntity<ReviewedProduct> reviewProduct(
            @RequestParam String userId,
            @RequestParam String productId,
            @RequestParam String review) {
        return ResponseEntity.ok(reviewedProductService.reviewProduct(userId, productId, review));
    }


    // Get all reviewed products for a user
    @GetMapping("/{userId}")
    public ResponseEntity<List<ReviewedProduct>> getReviewedProducts(@PathVariable String userId) {
        return ResponseEntity.ok(reviewedProductService.getReviewedProducts(userId));
    }

    // Unreview a product
    @DeleteMapping("/unreview")
    public ResponseEntity<String> unreviewProduct(@RequestParam String userId, @RequestParam String productId) {
        reviewedProductService.unreviewProduct(userId, productId);
        return ResponseEntity.ok("Product unreviewed successfully!");
    }

    // Get all reviewed products
    @GetMapping
    public ResponseEntity<List<ReviewedProduct>> getAllReviewedProducts() {
        return ResponseEntity.ok(reviewedProductService.getAllReviewedProducts());
    }
}
