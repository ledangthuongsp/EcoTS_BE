package com.example.EcoTS.DTOs.Request.User;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ChangePasswordRequest {
    private String token;
    private String oldPassword;
    private String newPassword;
}
