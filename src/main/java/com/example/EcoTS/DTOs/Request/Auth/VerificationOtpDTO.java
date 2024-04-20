package com.example.EcoTS.DTOs.Request.Auth;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VerificationOtpDTO {
    private String email;
    private Long otp;
}
