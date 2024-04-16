package com.example.EcoTS.DTOs.Request.Auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class SignInDTO {
    private String username;
    private String password;
}
