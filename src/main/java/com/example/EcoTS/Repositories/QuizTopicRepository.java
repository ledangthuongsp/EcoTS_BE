package com.example.EcoTS.Repositories;

import com.example.EcoTS.Models.QuizTopic;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Hidden
public interface QuizTopicRepository extends JpaRepository<QuizTopic, Long> {
}
