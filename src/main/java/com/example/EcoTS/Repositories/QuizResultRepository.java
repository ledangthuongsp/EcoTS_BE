package com.example.EcoTS.Repositories;

import com.example.EcoTS.Models.QuizResult;
import com.example.EcoTS.Models.QuizTopic;
import com.example.EcoTS.Models.Users;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Hidden
@Repository
public interface QuizResultRepository extends JpaRepository<QuizResult, Long> {
    List<QuizResult> findByUsers(Users users);
}
