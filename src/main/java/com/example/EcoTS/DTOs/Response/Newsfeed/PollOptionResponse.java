package com.example.EcoTS.DTOs.Response.Newsfeed;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PollOptionResponse {
    private Long id;
    private List<Long> voteIds;
    private String type;
}
