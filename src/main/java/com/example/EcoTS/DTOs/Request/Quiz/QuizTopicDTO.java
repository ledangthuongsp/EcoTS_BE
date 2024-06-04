package com.example.EcoTS.DTOs.Request.Quiz;

import lombok.Data;

import java.util.List;

@Data
public class QuizTopicDTO {
    private Long id;
    private String name;
    private String description;
    private String image;
    private double progress;
    private List<QuizQuestionDTO> questions;
}
