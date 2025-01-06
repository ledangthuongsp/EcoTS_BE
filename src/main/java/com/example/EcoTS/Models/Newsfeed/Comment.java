package com.example.EcoTS.Models.Newsfeed;

import com.example.EcoTS.Models.Users;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "newsfeed_id", nullable = false)
    Newsfeed newsfeed; // Bài viết được bình luận

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    Users user; // Người bình luận

    @Column(nullable = false, length = 300)
    String content; // Nội dung bình luận

    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    Comment parentComment; // Dùng để reply bình luận khác

    @CreationTimestamp
    LocalDateTime createdAt;
}
