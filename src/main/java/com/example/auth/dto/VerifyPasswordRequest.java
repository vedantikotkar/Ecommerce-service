package com.example.auth.dto;
import lombok.Data;

@Data
public class VerifyPasswordRequest {
    private String username;
    private String password;
}
