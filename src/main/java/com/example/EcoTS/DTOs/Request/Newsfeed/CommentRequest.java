package com.example.EcoTS.DTOs.Request.Newsfeed;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentRequest {
    private String content;
    private Long userId;
}
