package com.example.EcoTS.Repositories;

import com.example.EcoTS.Models.QuizTopic;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Hidden
public interface QuizTopicRepository extends JpaRepository<QuizTopic, Long> {
}
