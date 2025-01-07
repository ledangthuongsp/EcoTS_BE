package com.example.EcoTS.Models.Newsfeed;

import com.example.EcoTS.Models.Users;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private Long id;

    private String content;

    @ManyToOne
    private Users author;

    @ManyToOne
    private Newsfeed newsfeed;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    private List<ReplyComment> replies = new ArrayList<>();

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    private List<React> reactions = new ArrayList<>();

    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters and setters
}
