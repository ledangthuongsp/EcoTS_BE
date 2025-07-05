package com.example.EcoTS.Repositories;

import com.example.EcoTS.Models.OpeningSchedule;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;

@Hidden
public interface OpeningScheduleRepository extends JpaRepository<OpeningSchedule, Long> {
}
