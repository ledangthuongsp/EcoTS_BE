package com.example.EcoTS.Controllers.Donation;

import com.example.EcoTS.Repositories.DonationRepository;
import com.example.EcoTS.Services.DonationService.DonationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin
@Tag(name ="Donations", description = "this api for CRUD donations")
public class DonatePointsController {

    @Autowired
    private DonationRepository donationRepository;
    @Autowired
    private DonationService donationService;
}
