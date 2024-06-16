package com.example.EcoTS.Services.Quiz;

import com.example.EcoTS.Models.UserAnswer;
import com.example.EcoTS.Models.UserProgress;
import com.example.EcoTS.Repositories.QuizQuestionRepository;
import com.example.EcoTS.Repositories.UserAnswerRepository;
import com.example.EcoTS.Repositories.UserProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAnswerService {

    @Autowired
    private UserAnswerRepository userAnswerRepository;

    @Autowired
    private UserProgressRepository userProgressRepository;

    @Autowired
    private QuizQuestionRepository quizQuestionRepository;

    public UserAnswer saveUserAnswer(UserAnswer userAnswer) {
        UserAnswer savedAnswer = userAnswerRepository.save(userAnswer);
        updateUserProgress(userAnswer.getUserId(), userAnswer.getQuestionId());
        return savedAnswer;
    }

    public void deleteUserAnswer(Long id) {
        userAnswerRepository.deleteById(id);
    }

    private void updateUserProgress(Long userId, Long questionId) {
        // Fetch the question and topic information
        var quizQuestion = quizQuestionRepository.findById(questionId).orElseThrow();
        var topicId = quizQuestion.getQuizTopic().getId();

        // Fetch all answers of the user for the topic
        List<UserAnswer> userAnswers = userAnswerRepository.findByUserIdAndQuizQuestion_QuizTopic_Id(userId, topicId);

        // Calculate progress
        long correctAnswers = userAnswers.stream().filter(UserAnswer::isCorrect).distinct().count();
        long totalQuestions = quizQuestionRepository.countByQuizTopic_Id(topicId);

        // Update progress
        UserProgress userProgress = userProgressRepository.findByUserIdAndTopicId(userId, topicId)
                .orElse(new UserProgress());
        userProgress.setUserId(userId);
        userProgress.setTopicId(topicId);
        userProgress.setProgress((double) correctAnswers / totalQuestions * 100);

        userProgressRepository.save(userProgress);
    }
    public List<UserAnswer> getUserAnswersByTopic(Long userId, Long topicId) {
        return userAnswerRepository.findByUserIdAndQuizQuestion_QuizTopic_Id(userId, topicId);
    }
}
