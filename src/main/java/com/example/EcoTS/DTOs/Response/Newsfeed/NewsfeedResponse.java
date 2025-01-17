package com.example.EcoTS.DTOs.Response.Newsfeed;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class  NewsfeedResponse {
    private Long id;                      // ID của bài viết
    private String content;               // Nội dung bài viết
    private List<String> mediaUrls;       // Danh sách ảnh hoặc video
    private Long sponsorId;               // ID nhà tài trợ
    private double pointForActivity;      // Điểm hoạt động
    private Long userId;                  // ID người tạo bài viết
    private PollResponse poll;            // Thông tin về Poll (nếu có)
    private List<CommentResponse> comments; // Danh sách comment
    private List<ReactResponse> reacts;   // Danh sách react
}
