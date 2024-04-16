package com.example.EcoTS.DTOs.Response.Auth;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Hidden
public class LoginResponse {
    private String tokenAccess;
    private String tokenRefresh;
    private long expiresRefreshIn;
    private long expiresIn;
    private String username;
    private String role;
}
