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
    @Autowired
    private UserProgressService userProgressService;  // Service để xử lý tiến độ người dùng
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

        userProgressService.resetProgressForTopic(topicId);  // Reset progress của tất cả người dùng cho topic này
        return quizQuestionRepository.save(quizQuestion);
    }

    @Transactional
    public void deleteQuestion(Long questionId) {
        QuizQuestion quizQuestion = quizQuestionRepository.findById(questionId).orElseThrow(() -> new ResourceNotFoundException("Question not found"));
        QuizTopic topic = quizQuestion.getQuizTopic();
        topic.setNumberQuestion(topic.getNumberQuestion() - 1);
        quizQuestionRepository.delete(quizQuestion);
    }

    @Transactional
    public QuizQuestion updateQuestion(Long id, QuizQuestionDTO quizQuestionDTO) {
        QuizQuestion quizQuestion = quizQuestionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Question not found"));
        quizQuestion.setQuestionText(quizQuestionDTO.getQuestionText());
        quizQuestion.setCorrectAnswer(quizQuestionDTO.getCorrectAnswer());
        quizQuestion.setIncorrectAnswer1(quizQuestionDTO.getIncorrectAnswer1());
        quizQuestion.setIncorrectAnswer2(quizQuestionDTO.getIncorrectAnswer2());
        return quizQuestionRepository.save(quizQuestion);
    }

}
