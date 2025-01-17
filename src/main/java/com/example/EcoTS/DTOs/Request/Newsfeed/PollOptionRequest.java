package com.example.EcoTS.DTOs.Request.Newsfeed;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PollOptionRequest {
    private String type;
}
