package com.example.EcoTS.Repositories;

import com.example.EcoTS.Models.Results;
import com.example.EcoTS.Models.Users;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Hidden
public interface ResultRepository extends JpaRepository<Results, Long> {
    Optional<Results> findByUser(Users user);
    void deleteByUserId(Long userId);
}
