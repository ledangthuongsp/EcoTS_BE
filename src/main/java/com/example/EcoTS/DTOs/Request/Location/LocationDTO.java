package com.example.EcoTS.DTOs.Request.Location;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LocationDTO {
    private String description;
    private String locationName;
    private String typeOfLocation;
    private double latitude; // Vĩ độ
    private double longitude; // Kinh độ
}
