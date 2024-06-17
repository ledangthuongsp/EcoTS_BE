package com.example.EcoTS.Services.PointService;

import com.example.EcoTS.Models.*;
import com.example.EcoTS.Repositories.*;

import com.example.EcoTS.Services.Notification.NotificationService;
import com.example.EcoTS.Services.Statistic.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.transform.Result;
import java.awt.*;
import java.util.Optional;
@Service
public class PointService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PointRepository pointRepository;
    @Autowired
    private MaterialRepository materialRepository;
    @Autowired
    private ResultRepository resultRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private StatisticService statisticService;
    public void awardPoints(String barcodeData, double points) {
        String[] data = barcodeData.split(":");
        String username = data[0];
        Users user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Optional<Points> userPoints = pointRepository.findByUser(user);
        Points point = new Points();
        point = userPoints.get();
        point.setPoint(point.getPoint() + points);
        pointRepository.save(point);
    }

    public void awardPointsByUsernameAndEmail(String username, double points)
    {
        Users user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Optional<Points> userPoints = pointRepository.findByUser(user);
        Points point = new Points();
        point = userPoints.get();
        point.setPoint(point.getPoint() + points);
        pointRepository.save(point);
    }
    @Transactional
    public Points formAddPoints(String username, String email, Long employeeId, Double plasticKg, Double metalKg, Double clothKg,
                                Double glassKg, Double paperKg, Double cardboardKg ) {
        Users users = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Points points = pointRepository.findByUser(users)
                .orElseThrow(() -> new IllegalArgumentException("Point not found"));
        Results results = resultRepository.findByUser(users).orElseThrow(() -> new IllegalArgumentException("User not found"));
        double totalPoints = 0;
        double totalCo2Saved = 0;
        if (plasticKg != null && plasticKg > 0) {
            Materials material = materialRepository.findByName("PLASTIC").orElseThrow(() -> new IllegalArgumentException("Material not found"));
            totalPoints += material.getPointsPerKg() * plasticKg;
            totalCo2Saved += material.getCo2SavedPerKg() * plasticKg;
        }

        if (metalKg != null && metalKg > 0) {
            Materials material = materialRepository.findByName("METAL").orElseThrow(() -> new IllegalArgumentException("Material not found"));
            totalPoints += material.getPointsPerKg() * metalKg;
            totalCo2Saved += material.getCo2SavedPerKg() * metalKg;
        }

        if (clothKg != null && clothKg > 0) {
            Materials material = materialRepository.findByName("CLOTH").orElseThrow(() -> new IllegalArgumentException("Material not found"));
            totalPoints += material.getPointsPerKg() * clothKg;
            totalCo2Saved += material.getCo2SavedPerKg() * clothKg;
        }

        if (glassKg != null && glassKg > 0) {
            Materials material = materialRepository.findByName("GLASS").orElseThrow(() -> new IllegalArgumentException("Material not found"));
            totalPoints += material.getPointsPerKg() * glassKg;
            totalCo2Saved += material.getCo2SavedPerKg() * glassKg;
        }

        if (paperKg != null && paperKg > 0) {
            Materials material = materialRepository.findByName("PAPER").orElseThrow(() -> new IllegalArgumentException("Material not found"));
            totalPoints += material.getPointsPerKg() * paperKg;
            totalCo2Saved += material.getCo2SavedPerKg() * paperKg;
        }

        if (cardboardKg != null && cardboardKg > 0) {
            Materials material = materialRepository.findByName("CARDBOARD").orElseThrow(() -> new IllegalArgumentException("Material not found"));
            totalPoints += material.getPointsPerKg() * cardboardKg;
            totalCo2Saved += material.getCo2SavedPerKg() * cardboardKg;
        }

        points.setPoint(points.getPoint() + totalPoints);
        points.setTotalTrashCollect(points.getTotalTrashCollect() + plasticKg + metalKg + clothKg + glassKg + paperKg + cardboardKg);
        points.setSaveCo2(points.getSaveCo2() + totalCo2Saved);
        results.setMaximumPoints(totalPoints+results.getMaximumPoints());
        results.setSaveCo2(totalCo2Saved + results.getSaveCo2());
        pointRepository.save(points);

        notificationService.createNotification(users.getId(), totalPoints, employeeId);
        statisticService.saveStatistic(paperKg, cardboardKg, plasticKg, glassKg, clothKg, metalKg, employeeId);
        return points;
    }
    @Transactional
    public void addPoints(Long userId, double points) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Points userPoints = pointRepository.findByUser(user).orElseThrow(() -> new IllegalArgumentException("Point with this user not found"));
        Results results = resultRepository.findByUser(user).orElseThrow(() -> new IllegalArgumentException("Point with this user not found"));
        userPoints.setPoint(userPoints.getPoint() + points);
        pointRepository.save(userPoints);
        results.setMaximumPoints(results.getMaximumPoints()+points);
        resultRepository.save(results);
    }
}
