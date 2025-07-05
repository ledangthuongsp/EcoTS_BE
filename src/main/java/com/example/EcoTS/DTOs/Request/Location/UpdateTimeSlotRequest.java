package com.example.EcoTS.DTOs.Request.Location;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UpdateTimeSlotRequest {
    private Long id;
    private String startTime; // optional update
    private String endTime;
}
