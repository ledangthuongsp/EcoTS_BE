package com.example.EcoTS.DTOs.Response.Newsfeed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
@AllArgsConstructor
public class CommentAllReponse {
    private Long id;
    private Long userId;
    private String message;
    private List<String> imgUrls;
    private String userAvatarUrl;
    private String fullName;
}

