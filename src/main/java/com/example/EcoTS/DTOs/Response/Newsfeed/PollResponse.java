package com.example.EcoTS.DTOs.Response.Newsfeed;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PollResponse {
    private Long id;
    private String question;
    private List<PollOptionResponse> options;
}
