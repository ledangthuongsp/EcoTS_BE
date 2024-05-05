package com.example.EcoTS.Controllers.Point;

import com.example.EcoTS.Models.Points;
import com.example.EcoTS.Models.Users;
import com.example.EcoTS.Repositories.PointRepository;
import com.example.EcoTS.Repositories.UserRepository;
import com.example.EcoTS.Services.PointService.PointService;
import com.example.EcoTS.Services.SecurityService.JwtService;
import com.example.EcoTS.Services.UserService.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.awt.Point;
import java.util.Optional;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

@RestController
@CrossOrigin
@Tag(name ="Points CRUD", description = "this apis for changing and updating, adding points for users")
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

    @GetMapping("/point/get-user-point")
    public ResponseEntity<Points> getUserPoints(@RequestParam String token)
    {
        String username = jwtService.getUsername(token);
        Users user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Points userPoints = pointRepository.findByUserId(user.getId()).orElseThrow(() -> new IllegalArgumentException("Point with this user not found"));
        return ResponseEntity.ok(userPoints);
    }
    @PutMapping("/admin/point/add-user-points")
    public ResponseEntity<String> putUserPoints(@RequestParam String username, @RequestBody String email,@RequestParam double points) {
        pointService.awardPointsByUsernameAndEmail(username, points);
        return ResponseEntity.ok("Points added successfully.");
    }
//    @GetMapping("/admin/point/get-bar-code")
//    public ResponseEntity<Points> getUsernameAndEmailByBarcode(@RequestParam )
}
