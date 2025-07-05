package com.example.EcoTS.DTOs.Request.Location;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LocationSearchRequest {

    private Long materialId;
    private String day;
    private Double lat;
    private Double lng;
    private Double radiusKm = 5.0;
}
