package com.example.EcoTS.Repositories;

import com.example.EcoTS.Models.Points;

import com.example.EcoTS.Models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import java.awt.Point;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Hidden;

@Repository
@RepositoryRestResource
@Hidden
public interface PointRepository extends JpaRepository<Points, Long> {
    Optional<Points> findByUser(Users user);
    Optional<Points> findById(Long id);
    void deleteByUserId(Long userId);
}
