package com.example.auth.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "liked_products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LikedProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String productId;
    private String userId;
}
