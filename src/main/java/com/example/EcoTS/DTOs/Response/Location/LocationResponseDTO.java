package com.example.EcoTS.DTOs.Response.Location;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationResponseDTO {
    Long id;
    String locationName;
    String description;
    String typeOfLocation;
    String backGroundImgUrl;
    List<String> imgDetailsUrl;

    double latitude;
    double longitude;

    List<MaterialDTO> materials;
    List<OpeningScheduleDTO> openingSchedules;
    SponsorDTO sponsor;
}
