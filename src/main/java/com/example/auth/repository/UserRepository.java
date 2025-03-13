package com.example.auth.repository;

import com.example.auth.entity.Address;
import com.example.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    static Address save(Address address) {
        return address;
    }

    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u WHERE LOWER(TRIM(u.email)) = LOWER(TRIM(:email))")
    Optional<User> findByEmail(@Param("email") String email);
//    Optional<User> findByFileName(String fileName);

    Optional<User> findByProfilePhotoPath(String profilePhotoPath);
 //   User findByEmail(String email);
//    @Query("SELECT u FROM User u WHERE TRIM(u.email) = :email")
//    Optional<User> findByEmail(@Param("email") String email);

}
