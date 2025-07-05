package com.example.EcoTS.DTOs.Request.Location;

import com.example.EcoTS.DTOs.Response.Location.TimeSlotDTO;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddScheduleRequest {
    Long locationId;
    String dayOfWeek; // "MONDAY", "TUESDAY", ...
    List<UpdateTimeSlotRequest> timeSlots;
}
