package com.example.EcoTS.DTOs.Request.Auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordDTO {
    private String email;
    private String newPassword;
    private String confirmPassword;
    private Long otp;
}
