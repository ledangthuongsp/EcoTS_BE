package com.example.EcoTS.Repositories;

import com.example.EcoTS.Models.UserAnswer;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Hidden
public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {
    List<UserAnswer> findByUserIdAndQuizQuestion_QuizTopic_Id(Long userId, Long topicId);
}
