package com.example.EcoTS.Services.Statistic;

import com.example.EcoTS.Models.Materials;
import com.example.EcoTS.Models.Statistic;
import com.example.EcoTS.Repositories.MaterialRepository;
import com.example.EcoTS.Repositories.StatisticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticService {
    @Autowired
    private StatisticRepository statisticRepository;
    @Autowired
    private MaterialRepository materialsRepository;

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

    @Transactional
    public Map<String, Object> getStatisticsByPeriod(String period) {
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

        double totalCO2Saved = calculateTotalCO2Saved(result);

        Map<String, Object> response = new HashMap<>();
        response.put("statistics", result);
        response.put("totalCO2Saved", totalCO2Saved);

        return response;
    }
    @Transactional
    public double calculateTotalCO2Saved(Statistic statistics) {
        double totalCO2Saved = 0.0;

        totalCO2Saved += calculateCO2SavedForMaterial("paperKg", statistics.getPaperKg());
        totalCO2Saved += calculateCO2SavedForMaterial("cardboardKg", statistics.getCardBoardKg());
        totalCO2Saved += calculateCO2SavedForMaterial("plasticKg", statistics.getPlasticKg());
        totalCO2Saved += calculateCO2SavedForMaterial("glassKg", statistics.getGlassKg());
        totalCO2Saved += calculateCO2SavedForMaterial("clothKg", statistics.getClothKg());
        totalCO2Saved += calculateCO2SavedForMaterial("metalKg", statistics.getMetalKg());

        return totalCO2Saved;
    }

    private double calculateCO2SavedForMaterial(String materialType, double quantity) {
        Materials material = materialsRepository.findByType(materialType);
        if (material != null) {
            return material.getCo2SavedPerKg() * quantity;
        }
        return 0.0;
    }
}
