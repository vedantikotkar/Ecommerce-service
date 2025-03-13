package com.example.auth.service;

import com.example.auth.entity.CartProduct;
import com.example.auth.repository.CartProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartProductService {

    private final CartProductRepository cartProductRepository;

    public CartProductService(CartProductRepository cartProductRepository) {
        this.cartProductRepository = cartProductRepository;
    }

    // Add product to cart
    public CartProduct addToCart(String userId, String productId) {
        CartProduct cartProduct = new CartProduct();
        cartProduct.setUserId(userId);
        cartProduct.setProductId(productId);
        return cartProductRepository.save(cartProduct);
    }

    // Get all cart products for a user
    public List<CartProduct> getCartProducts(String userId) {
        return cartProductRepository.findByUserId(userId);
    }

    // Remove a product from the cart
    public void removeFromCart(String userId, String productId) {
        cartProductRepository.deleteByUserIdAndProductId(userId, productId);
    }

    // Get all cart products for all users
    public List<CartProduct> getAllCartProducts() {
        return cartProductRepository.findAll();
    }
}
