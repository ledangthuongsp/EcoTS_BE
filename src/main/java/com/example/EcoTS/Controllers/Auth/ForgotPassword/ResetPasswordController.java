package com.example.EcoTS.Controllers.Auth.ForgotPassword;

import com.example.EcoTS.DTOs.Request.Auth.ResetPasswordDTO;
import com.example.EcoTS.Services.SecurityService.AuthenticationService;

import com.example.EcoTS.Services.VerificationService.ResetPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@CrossOrigin
@Tag(name = "Authentication", description = "Authentication APIs")
public class ResetPasswordController {
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private ResetPasswordService resetPasswordService;
    @PutMapping("/auth/reset-password")
    public ResponseEntity<String> ResetPassword(@RequestBody ResetPasswordDTO resetPasswordRequest){
        String result = resetPasswordService.resetPassword(resetPasswordRequest.getEmail(), resetPasswordRequest.getOtp(), resetPasswordRequest.getNewPassword(), resetPasswordRequest.getConfirmPassword());
        return ResponseEntity.ok(result);
    }
}   
