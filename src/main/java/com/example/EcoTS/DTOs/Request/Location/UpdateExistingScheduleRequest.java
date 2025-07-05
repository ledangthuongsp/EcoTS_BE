package com.example.EcoTS.DTOs.Request.Location;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateExistingScheduleRequest {
    private Long scheduleId;
    private String dayOfWeek; // Chỉ dùng để cập nhật dayOfWeek của schedule
}
