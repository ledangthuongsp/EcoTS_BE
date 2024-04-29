package com.example.EcoTS.Controllers.Auth.SignIn;

import com.example.EcoTS.DTOs.Request.Auth.SignInDTO;
import com.example.EcoTS.DTOs.Response.Auth.LoginResponse;
import com.example.EcoTS.Models.Tokens;
import com.example.EcoTS.Models.Users;
import com.example.EcoTS.Repositories.TokenRepository;
import com.example.EcoTS.Services.SecurityService.AuthenticationService;
import com.example.EcoTS.Services.SecurityService.JwtRefreshService;
import com.example.EcoTS.Services.SecurityService.JwtService;

import org.springframework.http.HttpStatus;
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
public class SignInController {
    private final JwtService jwtService;
    private final JwtRefreshService jwtRefreshService;
    private final AuthenticationService authenticationService;
    private final TokenRepository tokenRepository;
    public SignInController(JwtService jwtService, JwtRefreshService jwtRefreshService, AuthenticationService authenticationService, TokenRepository tokenRepository) {
        this.jwtService = jwtService;
        this.jwtRefreshService = jwtRefreshService;
        this.authenticationService = authenticationService;
        this.tokenRepository = tokenRepository;
    }
    @PostMapping("/auth/signin")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody SignInDTO loginUserDto) {
        Users authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtTokenAccess = jwtService.generateToken(authenticatedUser);
        String jwtTokenRefresh = jwtRefreshService.generateToken(authenticatedUser);

        Tokens tokens = new Tokens();
        tokens.setUser(authenticatedUser);
        tokens.setTokenRefresh(jwtTokenRefresh);
        tokens.setTokenAccess(jwtTokenAccess);
        tokens.setExpireTime(jwtService.getExpirationTime());
        tokens.setExpireRefreshTime(jwtRefreshService.getExpirationTime());
        tokenRepository.save(tokens);

        //
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setTokenAccess(jwtTokenAccess);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());
        loginResponse.setUsername(authenticatedUser.getUsername());
        loginResponse.setRole(authenticatedUser.getRole());
        loginResponse.setTokenRefresh(jwtTokenRefresh);
        loginResponse.setExpiresRefreshIn(jwtRefreshService.getExpirationTime());
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }
}
