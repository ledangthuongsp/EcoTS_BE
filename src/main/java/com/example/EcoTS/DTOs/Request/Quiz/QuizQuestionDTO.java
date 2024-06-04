package com.example.EcoTS.DTOs.Request.Quiz;

import lombok.Data;

import java.util.List;

@Data
public class QuizQuestionDTO {
    private String questionText;
    private String correctAnswer;
    private String incorrectAnswer1;
    private String incorrectAnswer2;
}
