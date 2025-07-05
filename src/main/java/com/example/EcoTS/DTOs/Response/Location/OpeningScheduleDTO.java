package com.example.EcoTS.DTOs.Response.Location;

import java.util.List;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpeningScheduleDTO {
    String dayOfWeek; // "MONDAY", "TUESDAY", ...
    List<TimeSlotDTO> timeSlots;
}
