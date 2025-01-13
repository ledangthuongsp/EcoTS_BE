package com.example.EcoTS.DTOs.Response.Newsfeed;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class NewsfeedResponse {
    private Long id;
    private String content;
    private List<String> mediaUrls;
    private Long sponsorId;
    private double pointForActivity;
    private String createdBy; // "User" or "Sponsor"
    private Long createdById; // User or Sponsor ID
    private List<PollResponse> poll; // Lưu Poll (nếu có)
    private List<CommentResponse> comments; // Lưu Comment
    private List<ReactResponse> reactions; // Lưu React
}
