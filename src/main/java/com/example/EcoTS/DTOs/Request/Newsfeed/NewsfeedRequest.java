package com.example.EcoTS.DTOs.Request.Newsfeed;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class NewsfeedRequest {
    private Long id;
    private String content;
    private List<String> mediaUrls;
    private Long sponsorId;
    private double pointForActivity;
    private String createdBy; // "User" or "Sponsor"
    private Long createdById; // User or Sponsor ID
}
