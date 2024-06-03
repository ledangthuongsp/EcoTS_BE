package com.example.EcoTS.Models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "quiz_questions")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class QuizQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String questionText;

    @Column(nullable = false)
    String correctAnswer;

    @ElementCollection
    List<String> options;

    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = false)
    QuizTopic topic;
}
