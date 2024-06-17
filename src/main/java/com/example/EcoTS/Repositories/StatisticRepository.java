package com.example.EcoTS.Repositories;

import com.example.EcoTS.Models.Statistic;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Hidden
public interface StatisticRepository extends JpaRepository<Statistic, Long> {
}
