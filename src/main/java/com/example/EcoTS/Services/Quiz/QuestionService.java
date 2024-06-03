package com.example.EcoTS.Services.Quiz;

import com.example.EcoTS.Models.Question;
import com.example.EcoTS.Repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {
    @Autowired
    QuestionRepository questionRepository;

    public List<Question> getQuestionsByTopic(Long topicId) {
        return questionRepository.findByTopicId(topicId);
    }

    public Question createQuestion(Question question) {
        return questionRepository.save(question);
    }

    public Question updateQuestion(Question question) {
        return questionRepository.save(question);
    }

    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }
}
