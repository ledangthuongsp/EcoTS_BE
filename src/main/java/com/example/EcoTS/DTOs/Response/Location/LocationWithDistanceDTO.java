package com.example.EcoTS.DTOs.Response.Location;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocationWithDistanceDTO {
    private LocationResponseDTO location;
    private double distanceKm;
}