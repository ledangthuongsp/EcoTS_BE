package com.example.EcoTS.Controllers.Auth.SignUp;

import com.example.EcoTS.DTOs.Request.Auth.SignUpDTO;
import com.example.EcoTS.Models.Users;
import com.example.EcoTS.Services.SecurityService.AuthenticationService;
import com.example.EcoTS.Services.SecurityService.JwtService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@RestController
@CrossOrigin
@Tag(name = "Authentication", description = "Authentication APIs")
public class SignUpController {

    @Autowired
    private AuthenticationService authenticationService;
    @PostMapping("/auth/signup")
    public ResponseEntity<Users> register(@RequestBody SignUpDTO registerUserDto) {
        Users registeredUser = authenticationService.signup(registerUserDto);
        return ResponseEntity.ok().body(registeredUser);
    }
}
