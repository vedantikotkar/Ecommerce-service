package com.example.auth.repository;

import com.example.auth.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category,String> {
    // Additional query methods can be defined here if needed
}