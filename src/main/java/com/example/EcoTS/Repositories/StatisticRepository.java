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
    @Query("SELECT new com.example.EcoTS.Models.Statistic(" +
            "SUM(s.paperKg), SUM(s.cardBoardKg), SUM(s.plasticKg), SUM(s.glassKg), " +
            "SUM(s.clothKg), SUM(s.metalKg), SUM(s.saveCo2)) " +
            "FROM Statistic s WHERE s.createdAt BETWEEN :startDate AND :endDate")
    Statistic calculateStatistics(LocalDateTime startDate, LocalDateTime endDate);
    @Query("SELECT s FROM Statistic s WHERE s.createdAt >= :startDate AND s.createdAt <= :endDate")
    List<Statistic> findAllByDateRange(Timestamp startDate, Timestamp endDate);
    @Query("SELECT SUM(s.paperKg) as paperKg, SUM(s.cardBoardKg) as cardBoardKg, SUM(s.plasticKg) as plasticKg, " +
            "SUM(s.glassKg) as glassKg, SUM(s.clothKg) as clothKg, SUM(s.metalKg) as metalKg " +
            "FROM Statistic s WHERE s.createdAt >= :startDate AND s.createdAt <= :endDate")
    Statistic findStatisticsBetween(LocalDate startDate, LocalDate endDate);
}
