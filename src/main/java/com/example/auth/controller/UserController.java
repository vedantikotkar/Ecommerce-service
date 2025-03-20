package com.example.auth.controller;

import com.example.auth.dto.*;
import com.example.auth.entity.Address;
import com.example.auth.entity.User;
import com.example.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequest request) {
        authService.signup(request);
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

//    @PostMapping("/logout")
//    public ResponseEntity<String> logout(@RequestHeader("Authorization") String accessToken,
//                                         @RequestBody Map<String, String> requestBody) {
//        // Validate Authorization header format
//        if (!accessToken.startsWith("Bearer ")) {
//            return ResponseEntity.badRequest().body("Invalid token format. Expected 'Bearer <token>'.");
//        }
//
//        // Extract actual access token
//        String token = accessToken.substring(7);
//
//        // Extract refresh token from request body
//        String refreshToken = requestBody.get("refreshToken");
//        if (refreshToken == null || refreshToken.isEmpty()) {
//            return ResponseEntity.badRequest().body(" Refresh token is required.");
//        }
//
//        // Call logout method in AuthService
//        authService.logout(refreshToken, token);
//
//        return ResponseEntity.ok(" User logged out successfully.");
//
//}

//    @PostMapping("/forgot-password")
//    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
//        authService.forgotPassword(request);
//        return ResponseEntity.ok("Password reset email sent!");
//    }


    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        return authService.forgotPassword(email);
    }


    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok("Password changed successfully!");
    }



    @PostMapping("/verify-password")
    public ResponseEntity<String> verifyPassword(@RequestBody VerifyPasswordRequest request) {
        boolean isMatch = authService.verifyPassword(request.getUsername(), request.getPassword());

        if (isMatch) {
            return ResponseEntity.ok("Password is correct!");
        } else {
            return ResponseEntity.status(401).body("Incorrect password!");
        }
    }
    @GetMapping("/token")
    public ResponseEntity<String> getAdminToken() {
        try {
            String token = authService.getAdminToken();
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to fetch admin token: " + e.getMessage());
        }
    }

    @PostMapping("/save-address/{username}")
    public ResponseEntity<User> saveAddress(@PathVariable String username, @RequestBody Address address) {
        User updatedUser = authService.saveAddress(username, address);
        return ResponseEntity.ok(updatedUser);
    }


    @PostMapping("/upload")
    public ResponseEntity<String> uploadProfilePhoto(@RequestParam("file") MultipartFile file,
                                                     @RequestParam("username") String username) {
        try {
            String imageUrl = authService.uploadProfilePhoto(file, username);
            return ResponseEntity.ok("Profile photo uploaded successfully: " + imageUrl);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Failed to upload profile photo: " + e.getMessage());
        }
    }

    @DeleteMapping("/remove/{profilePhotoPath}")
    public ResponseEntity<String> removeProfilePhoto(@PathVariable String profilePhotoPath,
                                                     @RequestParam("username") String username) {
        boolean isDeleted = authService.removeProfilePhoto(profilePhotoPath, username);
        if (isDeleted) {
            return ResponseEntity.ok("Profile photo removed successfully.");
        } else {
            return ResponseEntity.badRequest().body("Failed to remove profile photo.");
        }
    }

    @GetMapping("/{profilePhotoPath}")
    public ResponseEntity<String> getImagePath(@PathVariable String profilePhotoPath) {
        User user = authService.getImageByProfilePhotoPath(profilePhotoPath);
        return ResponseEntity.ok(user.getProfilePhotoPath());
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String accessToken,
                                         @RequestParam String refreshToken) {
        authService.logout(refreshToken, accessToken);
        return ResponseEntity.ok("User logged out and all sessions removed.");
    }

        @PostMapping("/verify-email")
        public ResponseEntity<String> verifyEmail(@RequestParam String email) {
        return authService.verifyEmail(email);
        }
    }


