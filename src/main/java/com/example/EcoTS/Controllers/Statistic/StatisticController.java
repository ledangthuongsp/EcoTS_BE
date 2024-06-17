package com.example.EcoTS.Controllers.Statistic;

import com.example.EcoTS.Models.Statistic;
import com.example.EcoTS.Repositories.StatisticRepository;
import com.example.EcoTS.Services.Statistic.StatisticService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
@CrossOrigin
@Tag(name = "Statistic")
public class StatisticController {
    @Autowired
    private StatisticRepository statisticRepository;
    @Autowired
    private StatisticService statisticService;

    @GetMapping
    public ResponseEntity<List<Statistic>> getAllStatistics() {
        List<Statistic> statistics = statisticRepository.findAll();
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/by-period")
    public ResponseEntity<List<Map<String, Object>>> getStatisticsByPeriod(@RequestParam("period") String period) {
        List<Map<String, Object>> statistics = statisticService.getStatisticsByPeriod(period);
        return ResponseEntity.ok(statistics);
    }
}
