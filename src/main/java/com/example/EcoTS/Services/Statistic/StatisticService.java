package com.example.EcoTS.Services.Statistic;

import com.example.EcoTS.Models.Statistic;
import com.example.EcoTS.Repositories.StatisticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

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

    public Statistic getStatisticsByPeriod(String period) {
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime;

        switch (period.toLowerCase()) {
            case "month":
                startTime = endTime.minus(1, ChronoUnit.MONTHS);
                break;
            case "year":
                startTime = endTime.minus(1, ChronoUnit.YEARS);
                break;
            case "week":
            default:
                startTime = endTime.minus(1, ChronoUnit.WEEKS);
                break;
        }

        List<Statistic> statistics = statisticRepository.findAll();

        double totalPaperKg = 0.0;
        double totalCardboardKg = 0.0;
        double totalPlasticKg = 0.0;
        double totalGlassKg = 0.0;
        double totalClothKg = 0.0;
        double totalMetalKg = 0.0;

        for (Statistic stat : statistics) {
            if (stat.getCreatedAt().toLocalDateTime().isAfter(startTime) && stat.getCreatedAt().toLocalDateTime().isBefore(endTime)) {
                totalPaperKg += stat.getPaperKg();
                totalCardboardKg += stat.getCardBoardKg();
                totalPlasticKg += stat.getPlasticKg();
                totalGlassKg += stat.getGlassKg();
                totalClothKg += stat.getClothKg();
                totalMetalKg += stat.getMetalKg();
            }
        }

        Statistic result = new Statistic();
        result.setPaperKg(totalPaperKg);
        result.setCardBoardKg(totalCardboardKg);
        result.setPlasticKg(totalPlasticKg);
        result.setGlassKg(totalGlassKg);
        result.setClothKg(totalClothKg);
        result.setMetalKg(totalMetalKg);

        return result;
    }

    public double calculateTotalCO2Saved(Statistic statistics) {
        return (statistics.getPaperKg() * 0.46) +
                (statistics.getCardBoardKg() * 0.34) +
                (statistics.getPlasticKg() * 0.76) +
                (statistics.getGlassKg() * 0.38) +
                (statistics.getClothKg() * 0.3) +
                (statistics.getMetalKg() * 2.5);
    }
}
