package com.example.EcoTS.Repositories;

import com.example.EcoTS.Models.QuizQuestion;
import com.example.EcoTS.Models.QuizTopic;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Hidden
public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Long> {
    List<QuizQuestion> findByQuizTopic(QuizTopic quizTopic);
}
