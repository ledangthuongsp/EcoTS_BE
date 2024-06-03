package com.example.EcoTS.Services.Quiz;

import com.example.EcoTS.Models.QuizTopic;
import com.example.EcoTS.Repositories.QuizTopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizTopicService {
    @Autowired
    QuizTopicRepository quizTopicRepository;

    public List<QuizTopic> getAllQuizTopics() {
        return quizTopicRepository.findAll();
    }

    public QuizTopic getQuizTopicById(Long id) {
        return quizTopicRepository.findById(id).orElse(null);
    }

    public QuizTopic createQuizTopic(QuizTopic quizTopic) {
        quizTopic.setProgress(0.0); // Initialize progress
        return quizTopicRepository.save(quizTopic);
    }

    public QuizTopic updateQuizTopic(QuizTopic quizTopic) {
        return quizTopicRepository.save(quizTopic);
    }

    public void deleteQuizTopic(Long id) {
        quizTopicRepository.deleteById(id);
    }

    public void updateProgress(Long topicId, int correctAnswers, int totalQuestions) {
        QuizTopic quizTopic = getQuizTopicById(topicId);
        if (quizTopic != null) {
            double progress = ((double) correctAnswers / totalQuestions) * 100;
            quizTopic.setProgress(progress);
            updateQuizTopic(quizTopic);
        }
    }
}
