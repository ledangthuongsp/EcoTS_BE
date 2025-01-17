package com.example.EcoTS.DTOs.Request.Newsfeed;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@Getter
@Setter
public class CommentRequest {
    private Long userId;
    private String message;
}
