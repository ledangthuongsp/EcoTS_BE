package com.example.EcoTS.Services.PointService;

import com.example.EcoTS.Models.Materials;
import com.example.EcoTS.Models.Points;
import com.example.EcoTS.Models.Users;
import com.example.EcoTS.Repositories.MaterialRepository;
import com.example.EcoTS.Repositories.PointRepository;
import com.example.EcoTS.Repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class PointService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PointRepository pointRepository;
    @Autowired
    private MaterialRepository materialRepository;
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
    public void awardPointsByUsernameAndEmail(String username, double points)
    {
        Users user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Optional<Points> userPoints = pointRepository.findByUserId(user.getId());
        Points point = new Points();
        point = userPoints.get();
        point.setPoint(point.getPoint() + points);
        pointRepository.save(point);
    }
    public Points formAddPoints(String username, String email, String materialName, double totalTrashCollect) {
        Users users = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Points points = pointRepository.findByUserId(users.getId())
                .orElseThrow(() -> new IllegalArgumentException("Point not found"));
        Materials materials = materialRepository.findByName(materialName).orElseThrow(() -> new IllegalArgumentException("Material not found"));

        double pointsToAdd = materials.getPointsPerKg() * totalTrashCollect;
        double co2Saved = materials.getCo2SavedPerKg() * totalTrashCollect;
        points.setPoint(points.getPoint() + pointsToAdd);
        points.setTotalTrashCollect(points.getTotalTrashCollect() + totalTrashCollect);
        points.setSaveCo2(points.getSaveCo2() + co2Saved);
        pointRepository.save(points);
        return points;
    }
}
