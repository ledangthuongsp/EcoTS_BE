package com.example.EcoTS.Services.Quiz;

import com.example.EcoTS.Models.QuizResult;
import com.example.EcoTS.Models.QuizTopic;
import com.example.EcoTS.Repositories.QuizResultRepository;
import com.example.EcoTS.Repositories.QuizTopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizResultService {
    @Autowired
    QuizResultRepository quizResultRepository;

    @Autowired
    QuizTopicService quizTopicService;

    public List<QuizResult> getQuizResultsByUserAndQuiz(Long userId, Long quizId) {
        return quizResultRepository.findByUserIdAndQuizId(userId, quizId);
    }

    public QuizResult saveQuizResult(QuizResult quizResult) {
        QuizResult savedQuizResult = quizResultRepository.save(quizResult);
        updateQuizTopicProgress(quizResult);
        return savedQuizResult;
    }

    private void updateQuizTopicProgress(QuizResult quizResult) {
        List<QuizResult> results = getQuizResultsByUserAndQuiz(quizResult.getUserId(), quizResult.getQuizId());
        int maxScore = results.stream().mapToInt(QuizResult::getScore).max().orElse(0);

        // Assume we have a way to get total questions for the quiz
        int totalQuestions = 10; // Example value, replace with actual logic
        quizTopicService.updateProgress(quizResult.getQuizId(), maxScore, totalQuestions);
    }
}

