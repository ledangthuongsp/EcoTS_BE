package com.example.EcoTS.DTOs.Response.Newsfeed;

import com.example.EcoTS.DTOs.Request.Newsfeed.PollOptionRequest;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PollResponse {
    private String title;                // Tiêu đề poll
    private List<PollOptionRequest> options; // Danh sách các lựa chọn trong poll
}
