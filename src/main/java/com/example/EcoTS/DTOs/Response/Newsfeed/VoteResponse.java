package com.example.EcoTS.DTOs.Response.Newsfeed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class VoteResponse {
    public Long id;
    public Long userId;
    public String avatarUrl;
    public String fullName;
    public boolean status;
}
