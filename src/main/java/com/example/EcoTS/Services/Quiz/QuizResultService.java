package com.example.EcoTS.Services.Quiz;

import com.example.EcoTS.Models.QuizQuestion;
import com.example.EcoTS.Models.QuizResult;
import com.example.EcoTS.Models.QuizTopic;
import com.example.EcoTS.Models.Users;
import com.example.EcoTS.Repositories.QuizQuestionRepository;
import com.example.EcoTS.Repositories.QuizResultRepository;
import com.example.EcoTS.Repositories.QuizTopicRepository;
import com.example.EcoTS.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuizResultService {
    @Autowired
    private QuizResultRepository quizResultRepository;
    @Autowired
    private QuizQuestionRepository quizQuestionRepository;
    @Autowired
    private QuizTopicRepository quizTopicRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuizTopicService  quizTopicService;

    @Transactional
    public void saveQuizResult(Long userId, Long topicId, int correctAnswers, int incorrectAnswers) {

        QuizTopic quizTopic = quizTopicService.getTopicById(topicId);
        List<QuizQuestion> questions = quizQuestionRepository.findByQuizTopic(quizTopic);
        // Check if total questions match the sum of correct and incorrect answers
        if (correctAnswers + incorrectAnswers != questions.size()) {
            throw new IllegalArgumentException("Total questions must equal the sum of correct and incorrect answers");
        }
        double progress = (double) correctAnswers / questions.size();

        QuizResult quizResult = new QuizResult();
        quizResult.setTopicId(topicId);
        quizResult.setProgress(progress);
        quizResult.setUserId(userId);
        quizResult.setCorrectAnswers(correctAnswers);
        quizResult.setIncorrectAnswers(incorrectAnswers);
        quizResultRepository.save(quizResult);

        // Update the quiz topic progress if the new progress is higher
        if (progress > quizTopic.getProgress()) {
            quizTopic.setProgress(progress);
            quizTopicRepository.save(quizTopic);
        }
    }
}

