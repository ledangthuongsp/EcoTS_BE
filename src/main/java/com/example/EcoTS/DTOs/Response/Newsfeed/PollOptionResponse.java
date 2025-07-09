package com.example.EcoTS.DTOs.Response.Newsfeed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class PollOptionResponse {
    public Long id;
    public String type;
    public List<VoteResponse> votes;
}
