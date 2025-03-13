package com.example.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EmailRequest {
    // Getters and Setters
    private String name;
    private String email;
    private String phone;
    private String message;

}