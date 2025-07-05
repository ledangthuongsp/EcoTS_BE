package com.example.EcoTS.DTOs.Response.Location;
import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeSlotDTO {
    Long id;
    String startTime; // or LocalTime
    String endTime;
}
