package com.example.auth.service;

import com.example.auth.dto.*;
import com.example.auth.entity.Address;
import com.example.auth.entity.User;
import com.example.auth.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.keycloak.userprofile.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

import static org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties.UiService.LOGGER;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final WebClient webClient;
    private final PasswordEncoder passwordEncoder;
    private final String keycloakUrl = "http://localhost:8080";
    private final String realm = "master";
    private final String clientId = "springboot-keycloak";
    private final String clientSecret = "LYfLDQ6iMgR7edqmKtEgy2KNRJ37ac9O";
    private final String adminUsername = "admin";
    private final String adminPassword = "admin";
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    public AuthService(UserRepository userRepository, WebClient.Builder webClientBuilder) {
        this.userRepository = userRepository;
        this.webClient = webClientBuilder.baseUrl(keycloakUrl).build();
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public void signup(SignupRequest request) {
        String url = keycloakUrl + "/admin/realms/" + realm + "/users";

        Map<String, Object> user = new HashMap<>();
        user.put("username", request.getUsername());
        user.put("email", request.getEmail());
        user.put("firstName", request.getFirstName());
        user.put("lastName", request.getLastName());
        user.put("enabled", true);

        Map<String, Object> credentials = new HashMap<>();
        credentials.put("type", "password");
        credentials.put("value", request.getPassword());
        credentials.put("temporary", false);
        user.put("credentials", Collections.singletonList(credentials));

        try {
            ResponseEntity<String> response = webClient.post()
                    .uri(url)
                    .headers(headers -> headers.setBearerAuth(getAdminToken()))
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(user)
                    .retrieve()
                    .toEntity(String.class)
                    .block();

            if (response != null && response.getStatusCode().is2xxSuccessful()) {
                System.out.println(" User created successfully in Keycloak.");

                String location = response.getHeaders().getFirst("Location");

                if (location != null) {
                    String keycloakId = location.substring(location.lastIndexOf("/") + 1);
                    saveUserToDatabase(request, keycloakId);
                } else {
                    System.err.println("Failed to retrieve Keycloak ID.");
                }


            } else {
                System.err.println(" Failed to create user. Status: " +
                        (response != null ? response.getStatusCode() : "Unknown") +
                        ", Response: " + (response != null ? response.getBody() : "Unknown error"));
            }
        } catch (Exception e) {
            System.err.println("Exception during user signup: " + e.getMessage());
            throw new RuntimeException("Signup failed!", e);
        }
    }
    private void saveUserToDatabase(SignupRequest request ,String  keycloakId) {
        // Save the user to your local database
        User user = new User();
        user.setAuthIdReference(keycloakId);
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setEmailVerified(false); // Set emailVerified to false initially
        userRepository.save(user);
        System.out.println("User saved to local database.");
    }
    public String getAdminToken() {
        String tokenUrl = keycloakUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        try {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("client_id", clientId);
            formData.add("client_secret", clientSecret);
            formData.add("grant_type", "password");
            formData.add("username", adminUsername);
            formData.add("password", adminPassword);

            Map<String, Object> response = webClient.post()
                    .uri(tokenUrl)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response == null || !response.containsKey("access_token")) {
                throw new RuntimeException("Failed to fetch admin token. Response: " + response);
            }

            return response.get("access_token").toString();

        } catch (Exception e) {
            System.err.println(" Exception while fetching admin token: " + e.getMessage());
            throw new RuntimeException("Failed to fetch admin token", e);
        }
    }


    /**
     * Login User to Keycloak
     */
    public AuthResponse login(AuthRequest request) {
        String url = keycloakUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        // Validate username & password
        if (request.getUsername() == null || request.getUsername().isEmpty() ||
                request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new RuntimeException(" Username or password cannot be empty");
        }
        try {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("client_id", clientId);
            formData.add("client_secret", clientSecret);
            formData.add("grant_type", "password");
            formData.add("username", adminUsername);
            formData.add("password", adminPassword);

            Map<String, Object> response = webClient.post()
                    .uri(url)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response == null || !response.containsKey("access_token")) {
                throw new RuntimeException(" Invalid credentials! Login failed.");
            }

            return new AuthResponse(
                    (String) response.get("access_token"),
                    (String) response.get("refresh_token")
            );

        } catch (Exception e) {
            System.err.println(" Exception during login: " + e.getMessage());
            throw new RuntimeException(" Login failed! Please check credentials.", e);
        }
    }


    /**
     * Logout User from Keycloak
     */
    public String findUserIdByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(User::getId) // Assuming "id" is the Keycloak user ID
                .orElseThrow(() -> new RuntimeException(" User not found with username: " + username));
    }

    public void logout(String refreshToken, String accessToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new IllegalArgumentException("Refresh token cannot be empty.");
        }
        if (accessToken == null || accessToken.isEmpty()) {
            throw new IllegalArgumentException("Access token cannot be empty.");
        }

        // Step 1: Extract Username from Access Token
        String username = extractUsernameFromToken(accessToken);
        if (username == null) {
            throw new RuntimeException("Unable to extract username from access token.");
        }

        // Step 2: Get User ID from Database
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found.");
        }
        String keycloakId = String.valueOf(userOptional.get().getAuthIdReference());

        // Step 3: Logout using Refresh Token
        String logoutUrl = keycloakUrl + "/realms/" + realm + "/protocol/openid-connect/logout";
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("refresh_token", refreshToken);

        try {
            webClient.post()
                    .uri(logoutUrl)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .bodyValue(formData)
                    .retrieve()
                    .toBodilessEntity()
                    .block();

            LOGGER.info("User refresh token invalidated successfully.");

            // Step 4: Remove ALL User Sessions using POST instead of DELETE
            String adminToken = getAdminToken();
            String sessionUrl = keycloakUrl + "/admin/realms/" + realm + "/users/" + keycloakId + "/logout";

            webClient.post()
                    .uri(sessionUrl)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                    .retrieve()
                    .toBodilessEntity()
                    .block();

            LOGGER.info("All user sessions removed successfully.");

        } catch (WebClientResponseException e) {
            LOGGER.error("Logout failed: Status={}, Response={}", e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw new RuntimeException("Logout failed: " + e.getStatusCode(), e);
        } catch (Exception e) {
            LOGGER.error("Unexpected error during logout", e);
            throw new RuntimeException("Unexpected error during logout", e);
        }
    }



    // Method to extract username from JWT token
    private String extractUsernameFromToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                throw new IllegalArgumentException("Invalid JWT token.");
            }

            String payload = new String(Base64.getDecoder().decode(parts[1]));
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(payload);

            return jsonNode.has("preferred_username") ? jsonNode.get("preferred_username").asText() : null;
        } catch (Exception e) {
            System.err.println(" Failed to extract username from token: " + e.getMessage());
            return null;
        }
    }


    public void resetPassword(ResetPasswordRequest request) {
        try {
            // Fetch admin token
            String adminToken = getAdminToken();

            // Get user ID from Keycloak
            String userId = getUserId(request.getUsername(), adminToken);
            if (userId == null) {
                throw new RuntimeException("User not found: " + request.getUsername());
            }

            // Reset password in Keycloak
            updatePasswordInKeycloak(userId, request.getNewPassword(), adminToken);

            // Update password in local database
            Optional<User> userOptional = userRepository.findByUsername(request.getUsername());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setPassword(passwordEncoder.encode(request.getNewPassword())); // Encode password
                userRepository.save(user);
            } else {
                System.out.println("User not found in local database: " + request.getUsername());
            }

        } catch (WebClientResponseException e) {
            throw new RuntimeException("HTTP Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        } catch (Exception e) {
            throw new RuntimeException("Error resetting password for user: " + request.getUsername(), e);
        }
    }

    public boolean verifyPassword(String username, String enteredPassword) {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return passwordEncoder.matches(enteredPassword, user.getPassword()); // Compare password
        } else {
            throw new RuntimeException("User not found!");
        }
    }

    private String getUserId(String username, String adminToken) {
        String userSearchUrl = keycloakUrl + "/admin/realms/" + realm + "/users?username=" + username;

        List<Map<String, Object>> users = webClient.get()
                .uri(userSearchUrl)
                .headers(headers -> headers.setBearerAuth(adminToken))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
                .blockOptional()
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        return users.isEmpty() ? null : (String) users.get(0).get("id");
    }

    private void updatePasswordInKeycloak(String userId, String newPassword, String adminToken) {
        String resetUrl = keycloakUrl + "/admin/realms/" + realm + "/users/" + userId + "/reset-password";

        Map<String, Object> passwordPayload = new HashMap<>();
        passwordPayload.put("type", "password");
        passwordPayload.put("value", newPassword);
        passwordPayload.put("temporary", false);

        webClient.put()
                .uri(resetUrl)
                .headers(headers -> headers.setBearerAuth(adminToken))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(passwordPayload)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public ResponseEntity<String> forgotPassword(String email) {
        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Email cannot be empty");
        }

        String normalizedEmail = email.trim().toLowerCase();

        // Step 1: Get admin token
        String adminToken = getAdminToken();
        if (adminToken == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve admin token.");
        }

        // Step 2: Find user ID by email
        String userId = getUserIdByEmail(normalizedEmail);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        // Step 3: Trigger password reset email
        String resetPasswordUrl = keycloakUrl + "/admin/realms/" + realm + "/users/" + userId + "/execute-actions-email";
        List<String> actions = List.of("UPDATE_PASSWORD");

        try {
            webClient.put()
                    .uri(resetPasswordUrl)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .bodyValue(actions)
                    .retrieve()
                    .toBodilessEntity()
                    .block();

            return ResponseEntity.ok("Password reset email sent successfully.");
        } catch (WebClientResponseException e) {
            return ResponseEntity.status(e.getStatusCode()).body("Error triggering password reset: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }


    public ResponseEntity<String> verifyEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Email cannot be empty.");
        }

        // 🔹 Normalize Email
        String normalizedEmail = email.trim().toLowerCase();

        // 🔹 Get Admin Token
        String adminToken = getAdminToken();
        if (adminToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Failed to get admin access token.");
        }

        // 🔹 Find User ID in Keycloak
        String userId = getUserId(normalizedEmail, adminToken);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found in Keycloak.");
        }

        // 🔹 Mark Email as Verified in Keycloak
        String keycloakUserUrl = keycloakUrl + "/admin/realms/" + realm + "/users/" + userId;
        Map<String, Object> updatePayload = new HashMap<>();
        updatePayload.put("emailVerified", true);

        try {
            webClient.put()
                    .uri(keycloakUserUrl)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(updatePayload)
                    .retrieve()
                    .toBodilessEntity()
                    .block();

            // 🔹 Update Local Database
            Optional<User> userOptional = userRepository.findByEmail(normalizedEmail);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setEmailVerified(true);
                userRepository.save(user);
                return ResponseEntity.ok("Email verified successfully in Keycloak and database.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found in local database.");
            }

        } catch (WebClientResponseException e) {
            return ResponseEntity.status(e.getStatusCode()).body("Error verifying email: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    private String getUserIdByEmail(String email) {
        try {
            String url = keycloakUrl + "/admin/realms/" + realm + "/users?email=" + email;

            // Fetch admin token
            String adminToken = getAdminToken();
            if (adminToken == null) {
                throw new RuntimeException("Failed to retrieve admin token.");
            }

            // Make the request to fetch the user by email
            List<Map<String, Object>> users = webClient.get()
                    .uri(url)
                    .headers(headers -> headers.setBearerAuth(adminToken))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
                    .block();

            if (users != null && !users.isEmpty() && users.get(0).get("id") != null) {
                return users.get(0).get("id").toString();
            } else {
                return null;
            }
        } catch (WebClientResponseException e) {
            System.err.println("HTTP error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            return null;
        } catch (Exception e) {
            System.err.println("Error fetching user: " + e.getMessage());
            return null;
        }
    }

    @Transactional
    public User saveAddress(String username, Address address) {
        Optional<User> existingUser = userRepository.findByUsername(username);

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setAddress(address);  // Set address inside user
            return userRepository.save(user); // Save user with updated address
        } else {
            throw new RuntimeException("User not found!");
        }
    }

    private static final String UPLOAD_DIR = "src/main/resources/static/products/images/";


    public String uploadProfilePhoto(MultipartFile file, String username) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("File is empty");
        }

        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Path.of(UPLOAD_DIR + fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Save file path to database
        User userProfile = userRepository.findByUsername(username)
                .orElse(new User());
        userProfile.setUsername(username);
        userProfile.setProfilePhotoPath(fileName); // Save the path, not "fileName"
        userRepository.save(userProfile);

        return fileName;
    }

    public boolean removeProfilePhoto(String profilePhotoPath, String username) {
        File file = new File(UPLOAD_DIR + profilePhotoPath);
        boolean isDeleted = file.exists() && file.delete();

        if (isDeleted) {
            User userProfile = userRepository.findByUsername(username)
                    .orElse(null);
            if (userProfile != null) {
                userProfile.setProfilePhotoPath(null);
                userRepository.save(userProfile);
            }
        }

        return isDeleted;
    }

    public User getImageByProfilePhotoPath(String profilePhotoPath) {
        return userRepository.findByProfilePhotoPath(profilePhotoPath)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found"));
    }
}