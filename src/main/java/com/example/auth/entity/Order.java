//package com.example.auth.entity;
//
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//
//@Entity
//public class Order {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    private Long productId; // Reference to the Product ID
//    private int quantity;
//    private String status;
//
//    // Constructors, Getters, and Setters
//    public Order() {}
//
//    public Order(Long productId, int quantity, String status) {
//        this.productId = productId;
//        this.quantity = quantity;
//        this.status = status;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public Long getProductId() {
//        return productId;
//    }
//
//    public void setProductId(Long productId) {
//        this.productId = productId;
//    }
//
//    public int getQuantity() {
//        return quantity;
//    }
//
//    public void setQuantity(int quantity) {
//        this.quantity = quantity;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//}


package com.example.auth.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders")
@Schema(description = "Orders entity representing orders placed.")
public class  Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Unique ID of the order", example = "1")
    private String id;

    private String orderNo;

    private int item;
    private String userId;

    private String trackingCode;

    private String shippingService;

    @Column(nullable = false)
    @Schema(description = "productId")
    private String productId;

    @Column(nullable = false)
    @Schema(description = "Quantity of the product", example = "1")
    private int quantity;

    @Column(length = 1000)
    @Schema(description = "status of product ", example = "Delivered")
    private String status;

    private String currency;
    private double amount;

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