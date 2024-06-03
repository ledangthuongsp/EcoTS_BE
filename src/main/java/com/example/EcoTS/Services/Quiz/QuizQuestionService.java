package com.example.EcoTS.Services.Quiz;

import com.example.EcoTS.Models.QuizQuestion;
import com.example.EcoTS.Repositories.QuizQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizQuestionService {
    @Autowired
    QuizQuestionRepository quizQuestionRepository;

    public List<QuizQuestion> getQuizQuestionsByTopic(Long topicId) {
        return quizQuestionRepository.findByTopicId(topicId);
    }

    public QuizQuestion createQuizQuestion(QuizQuestion quizQuestion) {
        return quizQuestionRepository.save(quizQuestion);
    }

    public QuizQuestion updateQuizQuestion(QuizQuestion quizQuestion) {
        return quizQuestionRepository.save(quizQuestion);
    }

    public void deleteQuizQuestion(Long id) {
        quizQuestionRepository.deleteById(id);
    }
}
