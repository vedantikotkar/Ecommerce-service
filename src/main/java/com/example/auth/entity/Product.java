package com.example.auth.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "products")
@Schema(description = "Product entity representing items available in the store.")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Unique ID of the product", example = "1")
    private String id;

    @Column(nullable = false)
    @Schema(description = "Name of the product", example = "Nike Running Shoes")
    private String productName;

    @Column(nullable = false)
    @Schema(description = "Price of the product", example = "149.99")
    private double price;

    @Column(length = 1000)
    @Schema(description = "Customer reviews about the product", example = "Very comfortable and durable.")
    private String reviews;

    @ElementCollection
    @Schema(description = "Available colors for the product", example = "[\"Red\", \"Blue\", \"Black\"]")
    private List<String> availableColors;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;


    @Column(nullable = false, unique = true)
    @Schema(description = "Stock Keeping Unit (SKU) for inventory tracking", example = "SKU12345")
    private String sku;

    @Column(nullable = false)
    @Schema(description = "Discount percentage applied to the product", example = "10.5")
    private double discountPercentage;

    @Column(nullable = false)
    @Schema(description = "Total number of reviews", example = "120")
    private int reviewCount;

    @Column(nullable = false)
    @Schema(description = "Average customer rating", example = "4.5")
    private double rating;

    @ElementCollection
    @Schema(description = "Available sizes for the product", example = "[\"S\", \"M\", \"L\", \"XL\"]")
    private List<String> availableSizes;

    @Column(nullable = false)
    @Schema(description = "Material used in the product", example = "Leather")
    private String material;

    @Column(nullable = false)
    @Schema(description = "Brand of the product", example = "Nike")
    private String brand;

    @Column(nullable = false)
    @Schema(description = "Available quantity in stock", example = "50")
    private int quantity;

    @Column(nullable = false)
    @Schema(description = "Indicates if the product is returnable", example = "true")
    private boolean returnAvailable;

    @Column(nullable = false)
    @Schema(description = "Indicates if free delivery is available", example = "true")
    private boolean freeDelivery;

    @Column
    @Schema(description = "URL of the product image", example = "http://example.com/images/product123.jpg")
    private String imageUrl;

    // Additional fields
    @Column
    @Schema(description = "Detailed product description", example = "High-quality running shoes designed for comfort.")
    private String description;

    @Column
    @Schema(description = "Weight of the product in kilograms", example = "1.2")
    private double weight;

    @Column
    @Schema(description = "Dimensions of the product", example = "30cm x 20cm x 10cm")
    private String dimensions;

    @Column
    @Schema(description = "Shipping details for the product", example = "Ships within 3-5 business days.")
    private String shippingDetails;

    @Column
    @Schema(description = "Origin of the brand", example = "USA")
    private String brandOrigin;

    @Column
    @Schema(description = "Indicates if the product is active and available for purchase", example = "true")
    private boolean isActive = true;

    @Column
    @Schema(description = "Shipping cost for the product", example = "5.99")
    private double shippingCost;

    @Column
    @Schema(description = "Return policy details", example = "30-day return policy applicable.")
    private String returnPolicy;

    @ElementCollection
    @Schema(description = "Tags associated with the product for searchability", example = "[\"sports\", \"shoes\", \"running\"]")
    private List<String> tags;


    @ElementCollection
    @Schema(description = "Available images for the product")
    private List<String> images;

}
