package com.example.EcoTS.DTOs.Response.Location;
import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SponsorDTO
{
    Long id;
    String companyName;
    String avatarUrl;
}