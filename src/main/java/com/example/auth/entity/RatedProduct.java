package com.example.auth.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rated_products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RatedProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String productId; // Stores the product ID
    private String userId;    // Stores the user ID who liked the product
}
