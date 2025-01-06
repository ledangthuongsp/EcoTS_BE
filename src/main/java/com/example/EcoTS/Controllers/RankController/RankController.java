package com.example.EcoTS.Controllers.RankController;

import com.example.EcoTS.Services.UserService.RankService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rank")
@Tag(name = "User Ranking")
public class RankController {
    @Autowired
    private RankService rankService;
    @PostMapping("/{userId}/contribute")
    public ResponseEntity<String> contributePoints(@PathVariable Long userId, @RequestBody double contributedPoints) {
        rankService.contributePoints(userId, contributedPoints);
        return ResponseEntity.ok("Points contributed and rank updated successfully");
    }
}
