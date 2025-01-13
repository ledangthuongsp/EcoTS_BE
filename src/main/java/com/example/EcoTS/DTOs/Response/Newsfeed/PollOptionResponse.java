package com.example.EcoTS.DTOs.Response.Newsfeed;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PollOptionResponse {
    private Long id;
    private String optionText;
    private Long votesCount;
}
