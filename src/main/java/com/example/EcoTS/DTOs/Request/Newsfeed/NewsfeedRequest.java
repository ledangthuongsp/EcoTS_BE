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
public class NewsfeedRequest {
    private String content;           // Nội dung bài viết
    private Long sponsorId;           // ID nhà tài trợ
    private double pointForActivity;  // Điểm hoạt động
    private Long userId;              // ID người tạo bài viết
    private PollRequest poll;         // Thông tin Poll (nếu có)
}
