package com.example.EcoTS.DTOs.Request.Location;

import lombok.*;
import java.util.List;

@Data
public class AddScheduleRequest {
    Long locationId;
    String dayOfWeek; // "MONDAY", "TUESDAY", ...
    List<UpdateTimeSlotRequest> timeSlots;
}
