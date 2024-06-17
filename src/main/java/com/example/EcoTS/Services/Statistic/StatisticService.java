package com.example.EcoTS.Services.Statistic;

import com.example.EcoTS.Models.Statistic;
import com.example.EcoTS.Repositories.StatisticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class StatisticService {
    @Autowired
    private StatisticRepository statisticRepository;

    @Transactional
    public void saveStatistic(Double paperKg, Double cardboardKg, Double plasticKg, Double glassKg, Double clothKg, Double metalKg, Long employeeId) {
        Statistic statistic = new Statistic();
        statistic.setPaperKg(paperKg);
        statistic.setCardBoardKg(cardboardKg);
        statistic.setPlasticKg(plasticKg);
        statistic.setGlassKg(glassKg);
        statistic.setClothKg(clothKg);
        statistic.setMetalKg(metalKg);
        statistic.setEmployeeId(employeeId);
        statisticRepository.save(statistic);
    }

    public Statistic calculateStatistics(String period) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate;

        switch (period.toLowerCase()) {
            case "month":
                startDate = now.minus(1, ChronoUnit.MONTHS);
                break;
            case "year":
                startDate = now.minus(1, ChronoUnit.YEARS);
                break;
            case "week":
            default:
                startDate = now.minus(1, ChronoUnit.WEEKS);
                break;
        }

        return statisticRepository.calculateStatistics(startDate, now);
    }
}
