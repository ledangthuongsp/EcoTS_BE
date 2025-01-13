package com.example.EcoTS.DTOs.Request.Newsfeed;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReactRequest {
    private Long userId;
    private String reactionType;
}
