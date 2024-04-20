package com.example.EcoTS.Controllers.Donation;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.EcoTS.Models.Donations;
import com.example.EcoTS.Services.DonationService.DonationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@Controller
@CrossOrigin
@RequiredArgsConstructor
@Tag(name ="Donations", description = "this api for CRUD donations")
public class DonationCRUDController {

    @Autowired
    private DonationService donationService;
    @Autowired
    private Cloudinary cloudinary;
    @PostMapping("/donate/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, String> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            return ResponseEntity.ok(uploadResult.get("url"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Upload failed: " + e.getMessage());
        }
    }
    @PostMapping(value = "/donate/create-donation", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Donations> createVolunteer(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestPart("coverImage") List<MultipartFile> coverImage,
            @RequestPart("sponsorImages") List<MultipartFile> sponsorImages,
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate,
            @RequestParam("totalDonations") double totalDonations) throws IOException {
        Donations volunteer = donationService.createVolunteer(title, description, coverImage, sponsorImages, startDate, endDate, totalDonations);
        return ResponseEntity.ok(volunteer);
    }
}
