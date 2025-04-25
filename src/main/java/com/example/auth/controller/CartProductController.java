package com.example.auth.controller;

import com.example.auth.entity.CartProduct;
import com.example.auth.service.CartProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart-products")
public class CartProductController {

    private final CartProductService cartProductService;

    public CartProductController(CartProductService cartProductService) {
        this.cartProductService = cartProductService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestParam String userId, @RequestParam String productId) {
        try {
            return ResponseEntity.ok(cartProductService.addToCart(userId, productId));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }


    // Get all cart products for a user
    @GetMapping("/{userId}")
    public ResponseEntity<List<CartProduct>> getCartProducts(@PathVariable String userId) {
        return ResponseEntity.ok(cartProductService.getCartProducts(userId));
    }

    // Remove product from cart
    @PostMapping("/remove")
    public ResponseEntity<String> removeFromCart(@RequestParam String userId, @RequestParam String productId) {
        cartProductService.deleteCartProduct(userId, productId);
        return ResponseEntity.ok("Product removed from cart successfully!");
    }

    // Get all cart products for all users
    @GetMapping("/")
    public ResponseEntity<List<CartProduct>> getAllCartProducts() {
        return ResponseEntity.ok(cartProductService.getAllCartProducts());
    }
}
