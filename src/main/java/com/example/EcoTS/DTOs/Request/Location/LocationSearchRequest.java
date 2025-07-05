package com.example.EcoTS.DTOs.Request.Location;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LocationSearchRequest {

    @Schema(description = "ID của vật liệu", example = "3")
    private Long materialId;
    @Schema(description = "Thứ trong tuần", example = "MONDAY")
    private String day;
    @Schema(description = "Vĩ độ (latitude)", example = "10.8231")
    private Double lat;
    @Schema(description = "Kinh độ (longitude)", example = "106.6297")
    private Double lng;
    @Schema(description = "Bán kính (km)", example = "5.0")
    private Double radiusKm = 5.0;
}
