package com.example.auth.repository;

import com.example.auth.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, String> {

    @Query("SELECT w FROM Wishlist w WHERE w.userId = :userId")
    List<Wishlist> findByUserId(@Param("userId") String userId);
}
