package com.example.EcoTS.DTOs.Response.Newsfeed;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class VoteResponse {
    private Long id;
    private Long userId;
    private boolean status;
}
