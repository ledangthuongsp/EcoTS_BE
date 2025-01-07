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
    private Long id;

    private boolean liked;

    @ManyToOne
    private Users user;

    @ManyToOne
    private Newsfeed newsfeed;

    @ManyToOne
    private Comment comment;

    @ManyToOne
    private ReplyComment replyComment;

    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters and setters
}
