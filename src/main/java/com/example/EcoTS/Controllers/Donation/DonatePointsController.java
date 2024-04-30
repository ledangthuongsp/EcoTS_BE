package com.example.EcoTS.Controllers.Donation;

import com.example.EcoTS.Repositories.DonationRepository;
import com.example.EcoTS.Services.DonationService.DonationService;
import com.example.EcoTS.Services.SecurityService.JwtService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin
@RequestMapping("/donate")
@Tag(name ="Donations", description = "this api for CRUD donations")
public class DonatePointsController {

    @Autowired
    private DonationRepository donationRepository;
    @Autowired
    private DonationService donationService;
    @Autowired
    private JwtService jwtService;
    @PostMapping("/donate-points")
    public ResponseEntity<String> donatePoints(@RequestHeader("Authorization") String token,
                                               @PathVariable Long donationId,
                                               @RequestParam double points) {
        // Giải mã token và lấy username
        String username = jwtService.getUsername(token);
        // Thực hiện donatePoints
        donationService.donatePoints(username, donationId, points);
        return ResponseEntity.ok("Points donated successfully");
    }
}
