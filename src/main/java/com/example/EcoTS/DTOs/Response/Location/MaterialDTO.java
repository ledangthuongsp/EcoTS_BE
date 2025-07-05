package com.example.EcoTS.DTOs.Response.Location;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class MaterialDTO {
    Long id;
    String name;
    double pointsPerKg;
    double co2SavedPerKg;
    String type;
}