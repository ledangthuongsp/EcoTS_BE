package com.example.EcoTS.Services.Quiz;

import com.example.EcoTS.DTOs.Request.Quiz.QuizQuestionDTO;
import com.example.EcoTS.Models.QuizQuestion;
import com.example.EcoTS.Models.QuizTopic;
import com.example.EcoTS.Repositories.QuizQuestionRepository;
import com.example.EcoTS.Repositories.QuizTopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class QuizQuestionService {
    @Autowired
    private QuizQuestionRepository quizQuestionRepository;

    @Autowired
    private QuizTopicService quizTopicService;

    @Autowired
    private QuizTopicRepository quizTopicRepository;

    @Transactional
    public QuizQuestion addQuestion(Long topicId, QuizQuestionDTO quizQuestionDTO) {
        QuizQuestion quizQuestion = new QuizQuestion();
        QuizTopic topic = quizTopicRepository.findById(topicId).orElseThrow(() -> new ResourceNotFoundException("Topic not found"));
        quizQuestion.setQuestionText(quizQuestionDTO.getQuestionText());
        quizQuestion.setCorrectAnswer(quizQuestionDTO.getCorrectAnswer());
        quizQuestion.setIncorrectAnswer1(quizQuestionDTO.getIncorrectAnswer1());
        quizQuestion.setIncorrectAnswer2(quizQuestionDTO.getIncorrectAnswer2());
        quizQuestion.setQuizTopic(topic);
        topic.setNumberQuestion(topic.getNumberQuestion()+1);
        return quizQuestionRepository.save(quizQuestion);
    }

    public void deleteQuestion(Long questionId) {
        quizQuestionRepository.deleteById(questionId);
    }

    public Optional<QuizQuestion> getQuizQuestionById(Long id) {
        return quizQuestionRepository.findById(id);
    }

    public List<QuizQuestion> getAllQuizQuestions() {
        return quizQuestionRepository.findAll();
    }

    public void deleteQuizQuestion(Long id) {
        quizQuestionRepository.deleteById(id);
    }
}
