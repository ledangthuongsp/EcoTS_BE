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

import java.sql.Time;
import java.sql.Timestamp;
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
    private Double pointForActivity;
    private Long userId;
    private Long pollId;
    @ElementCollection
    private List<Long> commentIds = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "newsfeed_react_ids", joinColumns = @JoinColumn(name = "newsfeed_id"))
    @Column(name = "react_ids")
    private List<Long> reactIds = new ArrayList<>();


    @CreationTimestamp
    private Timestamp createdAt;
    @UpdateTimestamp
    private Timestamp updatedAt;

    private Timestamp startedAt;
    private Timestamp endedAt;
}

