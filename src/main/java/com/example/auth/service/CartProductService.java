package com.example.auth.service;

import com.example.auth.entity.CartProduct;
import com.example.auth.repository.CartProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartProductService {

    private final CartProductRepository cartProductRepository;

    public CartProductService(CartProductRepository cartProductRepository) {
        this.cartProductRepository = cartProductRepository;
    }

    public CartProduct addToCart(String userId, String productId) {
        Optional<CartProduct> existingProduct = cartProductRepository.findByUserIdAndProductId(userId, productId);
        if (existingProduct.isPresent()) {
            throw new IllegalStateException("Product already exists in the cart.");
        }
        CartProduct cartProduct = new CartProduct();
        cartProduct.setUserId(userId);
        cartProduct.setProductId(productId);
        return cartProductRepository.save(cartProduct);
    }


    // Get all cart products for a user
    public List<CartProduct> getCartProducts(String userId) {
        return cartProductRepository.findByUserId(userId);
    }

    @Transactional
    public void deleteCartProduct(String userId, String productId) {
        cartProductRepository.softDeleteByUserIdAndProductId(userId, productId);
    }

    // Get all cart products for all users
    public List<CartProduct> getAllCartProducts() {
        return cartProductRepository.findAll();
    }
}
