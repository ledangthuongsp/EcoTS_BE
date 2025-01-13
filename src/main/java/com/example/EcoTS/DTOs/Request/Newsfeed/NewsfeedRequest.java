package com.example.EcoTS.DTOs.Request.Newsfeed;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Builder
@Getter
@Setter
public class NewsfeedRequest {
    private Long id;
    private String content;
    private List<String> mediaUrls;
    private Long sponsorId;
    private double pointForActivity;
    private String createdBy; // "User" or "Sponsor"
    private Long createdById; // User or Sponsor ID
}
