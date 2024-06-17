package com.example.EcoTS.DTOs.Response.User;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class UsersDTO {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String address;
    private String personalId;
    private Timestamp dayOfBirth;
    private String avatarUrl;
    private String gender;
    private String role;
}
