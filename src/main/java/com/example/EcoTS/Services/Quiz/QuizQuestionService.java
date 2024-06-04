package com.example.EcoTS.Services.Quiz;

import com.example.EcoTS.DTOs.Request.Quiz.QuizQuestionDTO;
import com.example.EcoTS.Models.QuizQuestion;
import com.example.EcoTS.Models.QuizTopic;
import com.example.EcoTS.Repositories.QuizQuestionRepository;
import com.example.EcoTS.Repositories.QuizTopicRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizQuestionService {
    @Autowired
    private QuizQuestionRepository quizQuestionRepository;

    @Autowired
    private QuizTopicService quizTopicService;

    @Autowired
    private QuizTopicRepository quizTopicRepository;

    public QuizQuestion addQuestion(Long topicId, QuizQuestionDTO quizQuestionDTO) {
        QuizQuestion quizQuestion = new QuizQuestion();
        QuizTopic topic = quizTopicRepository.findById(topicId).orElseThrow(() -> new ResourceNotFoundException("Topic not found"));
        quizQuestion.setQuestionText(quizQuestionDTO.getQuestionText());
        quizQuestion.setCorrectAnswer(quizQuestionDTO.getCorrectAnswer());
        quizQuestion.setIncorrectAnswer1(quizQuestionDTO.getIncorrectAnswer1());
        quizQuestion.setIncorrectAnswer2(quizQuestionDTO.getIncorrectAnswer2());
        quizQuestion.setQuizTopic(topic);
        return quizQuestionRepository.save(quizQuestion);
    }

    public void deleteQuestion(Long questionId) {
        quizQuestionRepository.deleteById(questionId);
    }
}
