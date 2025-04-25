// ProductService.java
package com.example.auth.service;

import com.example.auth.entity.Product;
import com.example.auth.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getProductsByCategoryId(String categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> searchProducts(String keyword) {
        return productRepository.searchProducts(keyword);
    }

    public Product getProductById(String id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Product updateProduct(String id, Product updatedProduct) {
        Optional<Product> existingProduct = productRepository.findById(id);
        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();
            product.setProductName(updatedProduct.getProductName());
            product.setPrice(updatedProduct.getPrice());
            product.setReviews(updatedProduct.getReviews());
            product.setAvailableColors(updatedProduct.getAvailableColors());
            product.setQuantity(updatedProduct.getQuantity());
            product.setReturnAvailable(updatedProduct.isReturnAvailable());
            product.setFreeDelivery(updatedProduct.isFreeDelivery());
            product.setDeliveryAvailability(updatedProduct.getDeliveryAvailability());
            product.setImageUrl(updatedProduct.getImageUrl());
            return productRepository.save(product);
        } else {
            throw new RuntimeException("Product not found");
        }
    }

    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }

    public List<Product> bulkUploadProducts(List<Product> products) {
        return productRepository.saveAll(products);
    }


//    public Product increaseProductQuantity(String productId) {
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//
//        // Always increase by 1 (Hardcoded)
//        product.setQuantity(product.getQuantity() + 1);
//
//        // Modify price logic as needed (Keeping it unchanged)
//        product.setPrice(product.getPrice());
//
//        return productRepository.save(product);
//    }
//
//
//    public Product decreaseProductQuantity(String productId, int quantity) {
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//
//        if (product.getQuantity() < quantity) {
//            throw new RuntimeException("Not enough stock available");
//        }
//
//        product.setQuantity(product.getQuantity() - quantity);
//        product.setPrice(product.getPrice() - (quantity * product.getPrice()));
//
//        return productRepository.save(product);
//    }



    public Product increaseProductQuantity(String userId, String productId) {
        Product product = productRepository.findByIdAndUserId(productId, userId)
                .orElseThrow(() -> new RuntimeException("Product not found for this user"));

        product.setQuantity(product.getQuantity() + 1);
        return productRepository.save(product);
    }


    public Product decreaseProductQuantity(String userId, String productId) {
        Product product = productRepository.findByIdAndUserId(productId, userId)
                .orElseThrow(() -> new RuntimeException("Product not found for this user"));

        if (product.getQuantity() <= 0) {
            throw new RuntimeException("Not enough stock available");
        }

        product.setQuantity(product.getQuantity() - 1);
        return productRepository.save(product);
    }

}
