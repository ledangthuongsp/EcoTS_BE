package com.example.EcoTS.DTOs.Request.Location;

import com.example.EcoTS.Models.OpeningSchedule;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LocationSearchRequest {

    private Long materialId;
    private OpeningSchedule openingSchedule;
    private Double lat;
    private Double lng;
    private Double radiusKm = 5.0;
}
