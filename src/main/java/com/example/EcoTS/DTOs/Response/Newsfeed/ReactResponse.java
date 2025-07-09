package com.example.EcoTS.DTOs.Response.Newsfeed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ReactResponse {
    public Long id;
    public Long userId;
    public boolean status;
}
