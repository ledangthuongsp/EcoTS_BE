package com.example.EcoTS.DTOs.Request.Location;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateScheduleRequest {
    private Long locationId;
    private Long id;
    private String dayOfWeek; // optional update
    private List<UpdateTimeSlotRequest> timeSlots;
}
