package com.example.EcoTS.DTOs.Response.Newsfeed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private Long userId;
    private String message;
    private List<String> imgUrls;
}
