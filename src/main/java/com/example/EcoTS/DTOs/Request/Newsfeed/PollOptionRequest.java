package com.example.EcoTS.DTOs.Request.Newsfeed;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PollOptionRequest {
    private Long id;
    private String optionText;  // Nội dung của lựa chọn PollOption
    private List<Long> votedUserIds;  // Danh sách các user đã bình chọn
}
