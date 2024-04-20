package com.example.EcoTS.Controllers.Auth.ForgotPassword;

import com.example.EcoTS.Services.AuthService.VerificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin
@Tag(name = "Authentication", description = "Authentication APIs")
public class VerificationOtpController {
    @Autowired
    VerificationService verificationService;
    @PostMapping("/auth/check-otp")
    public ResponseEntity<String> checkOTP(@RequestParam Long codeOTP, @RequestParam String email) {
        if (verificationService.checkOTP(codeOTP, email)) {
            return ResponseEntity.ok("OTP is valid");
        } else {
            return ResponseEntity.badRequest().body("Invalid OTP or expired");
        }
    }
}
