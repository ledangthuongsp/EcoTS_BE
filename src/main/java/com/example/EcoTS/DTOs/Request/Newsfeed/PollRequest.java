package com.example.EcoTS.DTOs.Request.Newsfeed;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Builder
@Getter
@Setter
public class PollRequest {
    private String question;
    private List<String> options;
}
