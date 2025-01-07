package com.example.EcoTS.Models.Newsfeed;

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
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;
    @Column(nullable = false, length = 500)
    private String content; // Nội dung bài viết
    @ElementCollection
    private List<String> mediaUrls;

    @OneToMany(mappedBy = "newsfeed", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "newsfeed", cascade = CascadeType.ALL)
    private List<React> reactions = new ArrayList<>();

    @OneToOne(mappedBy = "newsfeed", cascade = CascadeType.ALL)
    private Poll poll;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private Long sponsorId;
    private double pointForActivity;
}
