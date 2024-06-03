package com.example.EcoTS.Services.Quiz;

import com.example.EcoTS.Models.QuizResult;
import com.example.EcoTS.Repositories.QuizResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizResultService {
    @Autowired
    QuizResultRepository quizResultRepository;

    @Autowired
    TopicService topicService;

    public List<QuizResult> getResultsByUserAndQuiz(Long userId, Long quizId) {
        return quizResultRepository.findByUserIdAndQuizId(userId, quizId);
    }

    public QuizResult saveResult(QuizResult result) {
        QuizResult savedResult = quizResultRepository.save(result);
        updateTopicProgress(result);
        return savedResult;
    }

    private void updateTopicProgress(QuizResult result) {
        List<QuizResult> results = getResultsByUserAndQuiz(result.getUserId(), result.getQuizId());
        int maxScore = results.stream().mapToInt(QuizResult::getScore).max().orElse(0);

        // Assume we have a way to get total questions for the quiz
        int totalQuestions = 10; // Example value, replace with actual logic
        topicService.updateProgress(result.getQuizId(), maxScore, totalQuestions);
    }
}
