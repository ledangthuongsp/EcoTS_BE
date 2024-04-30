package com.example.EcoTS.Services.PointService;

import com.example.EcoTS.Models.Points;
import com.example.EcoTS.Models.Users;
import com.example.EcoTS.Repositories.PointRepository;
import com.example.EcoTS.Repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class PointService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PointRepository pointRepository;

    public void awardPoints(String barcodeData, double points) {
        String[] data = barcodeData.split(":");
        String username = data[0];
        Users user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found"));

        Optional<Points> userPoints = pointRepository.findByUserId(user.getId());
        Points point = new Points();
        point = userPoints.get();
        point.setPoint(point.getPoint() + points);
        pointRepository.save(point);
    }
}
