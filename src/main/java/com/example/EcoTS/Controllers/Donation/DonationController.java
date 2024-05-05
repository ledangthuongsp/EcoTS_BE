package com.example.EcoTS.Controllers.Donation;

import com.example.EcoTS.Models.Donations;
import com.example.EcoTS.Repositories.DonationRepository;
import com.example.EcoTS.Services.DonationService.DonationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin
@RequestMapping("/donate")
@Tag(name ="Donations", description = "this api for CRUD donations")
public class DonationController {
    @Autowired
    private DonationService donationService;

    @GetMapping("/get-all-donations")
    public ResponseEntity<List<Donations>> donationGetAll()
    {
        List<Donations> donationsList= donationService.getAllDonation();
        if(donationsList.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(donationsList, HttpStatus.OK);
    }
}
