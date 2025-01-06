package com.example.EcoTS.Models.Newsfeed;

import com.example.EcoTS.Models.Users;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "react")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class React {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    Users user; // Người react

    @ManyToOne
    @JoinColumn(name = "newsfeed_id")
    Newsfeed newsfeed; // React trên bài viết

    @ManyToOne
    @JoinColumn(name = "comment_id")
    Comment comment; // React trên bình luận

    @Column(nullable = false)
    String type; // Loại react: like, love, haha...

    @CreationTimestamp
    LocalDateTime createdAt;
}
