package com.example.EcoTS.DTOs.Response.Newsfeed;

import com.example.EcoTS.DTOs.Request.Newsfeed.PollOptionRequest;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PollResponse {
    private Long id;
    private String title;
    private List<PollOptionResponse> pollOptions;
}
