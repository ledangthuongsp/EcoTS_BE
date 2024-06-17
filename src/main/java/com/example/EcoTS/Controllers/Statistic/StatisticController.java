package com.example.EcoTS.Controllers.Statistic;

import com.example.EcoTS.Models.Statistic;
import com.example.EcoTS.Repositories.StatisticRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/statistics")
@CrossOrigin
@Tag(name = "Statistic")
public class StatisticController {
    @Autowired
    private StatisticRepository statisticRepository;

    @GetMapping
    public ResponseEntity<List<Statistic>> getAllStatistics() {
        List<Statistic> statistics = statisticRepository.findAll();
        return ResponseEntity.ok(statistics);
    }
}
