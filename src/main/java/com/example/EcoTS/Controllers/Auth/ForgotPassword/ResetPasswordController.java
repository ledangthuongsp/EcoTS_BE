package com.example.EcoTS.Controllers.Auth.ForgotPassword;

import com.example.EcoTS.DTOs.Request.Auth.ResetPasswordDTO;
import com.example.EcoTS.Services.SecurityService.AuthenticationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@CrossOrigin
@Tag(name = "Authentication", description = "Authentication APIs")
public class ResetPasswordController {
    @Autowired
    private AuthenticationService authenticationService;
    @PutMapping("/auth/reset-password")
    public ResponseEntity<String> ResetPassword(@RequestParam  ResetPasswordDTO resetPasswordDTO){
        return new ResponseEntity<>(authenticationService.reset_password(resetPasswordDTO.getEmail(), resetPasswordDTO.getOtp(), resetPasswordDTO.getNewPassword(), resetPasswordDTO.getConfirmPassword()), HttpStatus.OK);
    }
}
