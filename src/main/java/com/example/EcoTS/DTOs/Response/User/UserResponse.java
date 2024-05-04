package com.example.EcoTS.DTOs.Response.User;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String role;
    private String avatarUrl;
    private Timestamp dayOfBirth;
    private String phoneNumber;
    private String address;
}
