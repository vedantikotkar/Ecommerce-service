package com.example.auth.repository;

import com.example.auth.entity.Emails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailsRepository extends JpaRepository<Emails, String> {
}
