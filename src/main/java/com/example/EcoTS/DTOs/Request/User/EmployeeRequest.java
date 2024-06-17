package com.example.EcoTS.DTOs.Request.User;

import lombok.Data;

import java.sql.Timestamp;
@Data
public class EmployeeRequest {
    private String fullName;
    private Timestamp dayOfBirth;
    private String gender;
    private String address;
    private String phoneNumber;
    private String personalId;
}
