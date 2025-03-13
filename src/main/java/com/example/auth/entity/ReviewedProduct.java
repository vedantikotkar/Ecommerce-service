package com.example.auth.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reviewed_products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewedProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String productId; // Stores the product ID
    private String userId;    // Stores the user ID who reviewed the product
}
