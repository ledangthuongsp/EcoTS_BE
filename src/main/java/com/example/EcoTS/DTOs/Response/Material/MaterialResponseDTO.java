package com.example.EcoTS.DTOs.Response.Material;

import lombok.Data;

@Data
public class MaterialResponseDTO {
    private Long id;
    private String name;
    private double pointsPerKg;
    private double co2SavedPerKg;
    private String type;
}
