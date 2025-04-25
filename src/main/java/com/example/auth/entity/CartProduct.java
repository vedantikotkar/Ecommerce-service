package com.example.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "cart_products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String productId; // Stores the product ID
    private String userId;    // Stores the user ID who added the product to the cart
    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    // Auditing Fields
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    private String createdBy;
    private String updatedBy;
}
