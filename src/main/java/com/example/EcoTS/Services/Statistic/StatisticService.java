package com.example.EcoTS.Services.Statistic;

import com.example.EcoTS.Models.Statistic;
import com.example.EcoTS.Repositories.StatisticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticService {
    @Autowired
    private StatisticRepository statisticRepository;

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
}
