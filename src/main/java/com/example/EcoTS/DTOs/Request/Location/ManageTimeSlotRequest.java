package com.example.EcoTS.DTOs.Request.Location;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ManageTimeSlotRequest {
    private Long scheduleId;
    private List<UpdateTimeSlotRequest> timeSlots; // Danh sách các TimeSlot để thêm hoặc cập nhật
}