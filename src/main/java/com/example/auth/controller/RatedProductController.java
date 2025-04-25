package com.example.auth.controller;

import com.example.auth.entity.RatedProduct;
import com.example.auth.service.RatedProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rated-products")
public class RatedProductController {

    private final RatedProductService ratedProductService;

    public RatedProductController(RatedProductService ratedProductService) {
        this.ratedProductService = ratedProductService;
    }

    @PostMapping("/rate")
    public ResponseEntity<RatedProduct> rateProduct(
            @RequestParam String userId,
            @RequestParam String productId,
            @RequestParam int rating) {
        return ResponseEntity.ok(ratedProductService.rateProduct(userId, productId, rating));
    }


    // Get all rated products for a user
    @GetMapping("/{userId}")
    public ResponseEntity<List<RatedProduct>> getRatedProducts(@PathVariable String userId) {
        return ResponseEntity.ok(ratedProductService.getRatedProducts(userId));
    }

    // Unrate a product
    @DeleteMapping("/unrate")
    public ResponseEntity<String> unrateProduct(@RequestParam String userId, @RequestParam String productId) {
        ratedProductService.unrateProduct(userId, productId);
        return ResponseEntity.ok("Product unrated successfully!");
    }

    // Get all rated products
    @GetMapping
    public ResponseEntity<List<RatedProduct>> getAllRatedProducts() {
        return ResponseEntity.ok(ratedProductService.getAllRatedProducts());
    }
}
