package com.example.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

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
    private String productId;
    private String userId;
    private int rating;
    @Column(name = "is_deleted")
    private Boolean isDeleted; // Use wrapper class


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
