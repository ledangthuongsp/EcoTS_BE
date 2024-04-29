package com.example.EcoTS.Repositories;

import com.example.EcoTS.Models.Points;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import java.awt.Point;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Hidden;

@Repository
@Hidden
public interface PointRepository extends JpaRepository<Points, Long> {
    Optional<Points> findById(Long id);
    Optional<Points> findByUserId(Long userId);
}
