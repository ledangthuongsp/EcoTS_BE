package com.example.EcoTS.Controllers.Statistic;

import com.example.EcoTS.Models.Statistic;
import com.example.EcoTS.Repositories.StatisticRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
    @GetMapping("/get-by-time")
    public ResponseEntity<List<Statistic>> getStatistics(@RequestParam String period) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate;

        switch (period) {
            case "Month":
                startDate = now.minus(1, ChronoUnit.MONTHS);
                break;
            case "Year":
                startDate = now.minus(1, ChronoUnit.YEARS);
                break;
            case "Week":
            default:
                startDate = now.minus(1, ChronoUnit.WEEKS);
                break;
        }

        List<Statistic> statistics = statisticRepository.findAllByDateRange(
                Timestamp.valueOf(startDate),
                Timestamp.valueOf(now)
        );
        return ResponseEntity.ok(statistics);
    }

}
