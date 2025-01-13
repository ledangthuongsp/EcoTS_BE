package com.example.EcoTS.DTOs.Response.Newsfeed;

import java.util.List;

public class ReactResponse {
    private String content;
    private List<String> mediaUrls;
    private Long sponsorId;
    private double pointForActivity;
    private String createdBy; // "User" or "Sponsor"
    private Long createdById; // User or Sponsor ID
    private List<Long> pollOptionIds; // ID các option của Poll
}
