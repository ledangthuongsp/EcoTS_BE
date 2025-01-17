package com.example.EcoTS.DTOs.Request.Newsfeed;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class VoteRequest {
    private Long userId;
    private boolean status;
}
