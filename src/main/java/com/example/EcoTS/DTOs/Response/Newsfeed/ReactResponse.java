package com.example.EcoTS.DTOs.Response.Newsfeed;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReactResponse {
    private Long id;
    private Long userId;
    private boolean status;
}
