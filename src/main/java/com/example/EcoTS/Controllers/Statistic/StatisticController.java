package com.example.EcoTS.Controllers.Statistic;

import com.example.EcoTS.Models.Statistic;
import com.example.EcoTS.Services.Statistic.StatisticService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
@CrossOrigin
@Tag(name = "Statistic")
public class StatisticController {
    @Autowired
    private StatisticService statisticService;

    @GetMapping("/by-period")
    public ResponseEntity<List<Map<String, Object>>> getStatisticsByPeriod(@RequestParam("period") String period) {
        List<Map<String, Object>> statistics = statisticService.getStatisticsByPeriod(period);
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/total")
    public ResponseEntity<Statistic> getTotalStatistics() {
        Statistic totalStatistics = statisticService.getTotalStatistics();
        return ResponseEntity.ok(totalStatistics);
    }

    @GetMapping("/current-week")
    public ResponseEntity<Statistic> getCurrentWeekStatistics() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfWeek = now.minusDays(now.getDayOfWeek().getValue() - 1);
        Timestamp startTimestamp = Timestamp.valueOf(startOfWeek);
        Timestamp endTimestamp = Timestamp.valueOf(now);
        List<Statistic> statistics = statisticService.getStatisticsByPeriod(startTimestamp, endTimestamp);
        return ResponseEntity.ok(statistics.isEmpty() ? new Statistic() : statistics.get(0));
    }

    @GetMapping("/last-week")
    public ResponseEntity<Statistic> getLastWeekStatistics() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfLastWeek = now.minusWeeks(1).with(java.time.DayOfWeek.MONDAY);
        LocalDateTime endOfLastWeek = now.with(java.time.DayOfWeek.SUNDAY);
        Timestamp startTimestamp = Timestamp.valueOf(startOfLastWeek);
        Timestamp endTimestamp = Timestamp.valueOf(endOfLastWeek);
        List<Statistic> statistics = statisticService.getStatisticsByPeriod(startTimestamp, endTimestamp);
        return ResponseEntity.ok(statistics.isEmpty() ? new Statistic() : statistics.get(0));
    }
}
