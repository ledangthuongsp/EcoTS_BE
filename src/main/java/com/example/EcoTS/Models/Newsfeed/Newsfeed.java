package com.example.EcoTS.Models.Newsfeed;

import com.example.EcoTS.DTOs.Response.Newsfeed.CommentResponse;
import com.example.EcoTS.DTOs.Response.Newsfeed.PollResponse;
import com.example.EcoTS.DTOs.Response.Newsfeed.ReactResponse;
import com.example.EcoTS.Models.Users;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "newsfeed")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Newsfeed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private List<String> mediaUrls;
    private Long sponsorId;
    private double pointForActivity;
    private String createdBy; // "User" or "Sponsor"
    private Long createdById; // User or Sponsor ID
    // Quan hệ với Poll (một Newsfeed có thể có một Poll)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "newsfeed", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Poll poll;

    // Quan hệ với Comment (một Newsfeed có thể có nhiều comment)
    @OneToMany(mappedBy = "newsfeed", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Comment> comments = new ArrayList<>();

    // Quan hệ với React (một Newsfeed có thể có nhiều react)
    @OneToMany(mappedBy = "newsfeed", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<React> reactions = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;
}

