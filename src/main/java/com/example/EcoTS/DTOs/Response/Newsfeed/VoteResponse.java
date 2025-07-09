package com.example.EcoTS.DTOs.Response.Newsfeed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class VoteResponse {
    private Long id;
    private Long userId;
    private String avatarUrl;
    private String fullName;
    private boolean status;
}
