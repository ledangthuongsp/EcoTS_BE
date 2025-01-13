package com.example.EcoTS.Controllers.Point;

import com.example.EcoTS.Models.Points;
import com.example.EcoTS.Models.Results;
import com.example.EcoTS.Models.Users;
import com.example.EcoTS.Repositories.PointRepository;
import com.example.EcoTS.Repositories.ResultRepository;
import com.example.EcoTS.Repositories.UserRepository;
import com.example.EcoTS.Services.PointService.PointService;
import com.example.EcoTS.Services.SecurityService.JwtService;
import com.example.EcoTS.Services.UserService.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.Point;
import java.util.Optional;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

@RestController
@CrossOrigin
@Tag(name ="Points CRUD", description = "this apis for changing and updating, adding points for users")
@RequestMapping("/point")
public class PointController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PointRepository pointRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private PointService pointService;
    @Autowired
    private ResultRepository resultRepository;

    @GetMapping("/get-user-point")
    public ResponseEntity<Points> getUserPoints(@RequestParam String token)
    {

        String username = jwtService.getUsername(token);
        Users user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Points userPoints = pointRepository.findByUser(user).orElseThrow(() -> new IllegalArgumentException("Point with this user not found"));
        return ResponseEntity.ok(userPoints);
    }
    @PutMapping("/admin/add-user-points-by-form")
    public ResponseEntity<Points> addUserPointsByForm(@RequestParam String username, @RequestParam String email,
                                                      @RequestParam Long employeeId,
                                                      @RequestParam(required = false) Double plasticKg,
                                                      @RequestParam(required = false) Double metalKg,
                                                      @RequestParam(required = false) Double clothKg,
                                                      @RequestParam(required = false) Double glassKg,
                                                      @RequestParam(required = false) Double paperKg,
                                                      @RequestParam(required = false) Double cardboardKg) {
        Points points = pointService.formAddPoints(username, email, employeeId, plasticKg, metalKg, clothKg, glassKg, paperKg, cardboardKg);
        return ResponseEntity.ok(points);
    }
    @PutMapping("/complete-quiz-add-points")
    public void addPointsByQuiz(@RequestParam Long userId, @RequestParam double points)
    {
        pointService.addPoints(userId, points);
    }
}

