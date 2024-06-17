package com.example.EcoTS.Services.Statistic;

import com.example.EcoTS.Models.Materials;
import com.example.EcoTS.Models.Statistic;
import com.example.EcoTS.Repositories.MaterialRepository;
import com.example.EcoTS.Repositories.StatisticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class StatisticService {
    @Autowired
    private StatisticRepository statisticRepository;
    @Autowired
    private MaterialRepository materialRepository;

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

    public List<Statistic> getStatisticsByPeriod(Timestamp startTime, Timestamp endTime) {
        return statisticRepository.findAllByCreatedAtBetween(startTime, endTime);
    }

    public Statistic getTotalStatistics() {
        List<Statistic> statistics = statisticRepository.findAll();

        double totalPaperKg = 0.0;
        double totalCardboardKg = 0.0;
        double totalPlasticKg = 0.0;
        double totalGlassKg = 0.0;
        double totalClothKg = 0.0;
        double totalMetalKg = 0.0;

        for (Statistic stat : statistics) {
            totalPaperKg += stat.getPaperKg();
            totalCardboardKg += stat.getCardBoardKg();
            totalPlasticKg += stat.getPlasticKg();
            totalGlassKg += stat.getGlassKg();
            totalClothKg += stat.getClothKg();
            totalMetalKg += stat.getMetalKg();
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

    public List<Map<String, Object>> getStatisticsByPeriod(String period) {
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime;

        int timeUnits;
        switch (period.toLowerCase()) {
            case "month":
                timeUnits = 12;
                startTime = endTime.minus(timeUnits, ChronoUnit.MONTHS);
                break;
            case "year":
                timeUnits = 2;
                startTime = endTime.minus(timeUnits, ChronoUnit.YEARS);
                break;
            case "week":
            default:
                timeUnits = 24;
                startTime = endTime.minus(timeUnits, ChronoUnit.WEEKS);
                break;
        }

        Timestamp startTimestamp = Timestamp.valueOf(startTime);
        Timestamp endTimestamp = Timestamp.valueOf(endTime);

        List<Statistic> statistics = statisticRepository.findAllByCreatedAtBetween(startTimestamp, endTimestamp);
        List<Map<String, Object>> result = new ArrayList<>();

        for (int i = 0; i < timeUnits; i++) {
            LocalDateTime periodStart = endTime.minus(i + 1, ChronoUnit.valueOf(period.toUpperCase() + "S"));
            LocalDateTime periodEnd = endTime.minus(i, ChronoUnit.valueOf(period.toUpperCase() + "S"));

            Timestamp periodStartTimestamp = Timestamp.valueOf(periodStart);
            Timestamp periodEndTimestamp = Timestamp.valueOf(periodEnd);

            double totalPaperKg = 0.0;
            double totalCardboardKg = 0.0;
            double totalPlasticKg = 0.0;
            double totalGlassKg = 0.0;
            double totalClothKg = 0.0;
            double totalMetalKg = 0.0;

            for (Statistic stat : statistics) {
                if (stat.getCreatedAt().after(periodStartTimestamp) && stat.getCreatedAt().before(periodEndTimestamp)) {
                    totalPaperKg += stat.getPaperKg();
                    totalCardboardKg += stat.getCardBoardKg();
                    totalPlasticKg += stat.getPlasticKg();
                    totalGlassKg += stat.getGlassKg();
                    totalClothKg += stat.getClothKg();
                    totalMetalKg += stat.getMetalKg();
                }
            }

            Map<String, Object> periodResult = new HashMap<>();
            periodResult.put("periodStart", periodStartTimestamp);
            periodResult.put("periodEnd", periodEndTimestamp);
            periodResult.put("paperKg", totalPaperKg);
            periodResult.put("cardboardKg", totalCardboardKg);
            periodResult.put("plasticKg", totalPlasticKg);
            periodResult.put("glassKg", totalGlassKg);
            periodResult.put("clothKg", totalClothKg);
            periodResult.put("metalKg", totalMetalKg);
            periodResult.put("co2Saved", calculateTotalCO2Saved(totalPaperKg, totalCardboardKg, totalPlasticKg, totalGlassKg, totalClothKg, totalMetalKg));

            result.add(periodResult);
        }

        return result;
    }

    public double calculateTotalCO2Saved(double paperKg, double cardboardKg, double plasticKg, double glassKg, double clothKg, double metalKg) {
        List<Materials> materials = materialRepository.findAll();
        double totalCO2Saved = 0.0;

        for (Materials material : materials) {
            switch (material.getType()) {
                case "paperKg":
                    totalCO2Saved += paperKg * material.getCo2SavedPerKg();
                    break;
                case "cardboardKg":
                    totalCO2Saved += cardboardKg * material.getCo2SavedPerKg();
                    break;
                case "plasticKg":
                    totalCO2Saved += plasticKg * material.getCo2SavedPerKg();
                    break;
                case "glassKg":
                    totalCO2Saved += glassKg * material.getCo2SavedPerKg();
                    break;
                case "clothKg":
                    totalCO2Saved += clothKg * material.getCo2SavedPerKg();
                    break;
                case "metalKg":
                    totalCO2Saved += metalKg * material.getCo2SavedPerKg();
                    break;
            }
        }

        return totalCO2Saved;
    }
}
