package com.example.auth.dto;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String username;
    private String newPassword;
}
