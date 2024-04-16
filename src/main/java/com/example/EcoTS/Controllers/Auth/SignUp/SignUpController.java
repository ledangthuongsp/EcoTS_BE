package com.example.EcoTS.Controllers.Auth.SignUp;

import com.example.EcoTS.DTOs.Request.Auth.SignUpDTO;
import com.example.EcoTS.Models.Users;
import com.example.EcoTS.Services.SecurityService.AuthenticationService;
import com.example.EcoTS.Services.SecurityService.JwtService;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
@CrossOrigin
public class SignUpController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public SignUpController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<Users> register(@RequestBody SignUpDTO registerUserDto) {
        Users registeredUser = authenticationService.signup(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }
}
