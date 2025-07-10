package com.example.EcoTS.DTOs.Request.Sponsor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SponsorLoginRequest {
    private String email;
    private String password;
}
