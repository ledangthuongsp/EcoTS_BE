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
    public Long id;
    public Long userId;
    public String message;
    public List<String> imgUrls;
}
