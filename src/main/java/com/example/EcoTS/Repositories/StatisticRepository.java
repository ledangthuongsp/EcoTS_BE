package com.example.EcoTS.Repositories;

import com.example.EcoTS.Models.Statistic;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Hidden
public interface StatisticRepository extends JpaRepository<Statistic, Long> {
}
