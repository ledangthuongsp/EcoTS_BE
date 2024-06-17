package com.example.EcoTS.Controllers.Donation;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.EcoTS.Models.Donations;
import com.example.EcoTS.Repositories.DonationHistoryRepository;
import com.example.EcoTS.Repositories.DonationRepository;
import com.example.EcoTS.Services.DonationService.DonationService;

import net.bytebuddy.asm.Advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@Tag(name ="Donations", description = "this api for CRUD donations")
public class DonationCRUDController {
    private static final Logger logger = LoggerFactory.getLogger(DonationController.class);
    @Autowired
    private DonationService donationService;
    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private DonationRepository donationRepository;
    @PostMapping(value = "/admin/donate/create-donation", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Donations> createVolunteer(
            @RequestParam("title") String title,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestPart("coverImage") List<MultipartFile> coverImage,
            @RequestPart("sponsorImages") List<MultipartFile> sponsorImages,
            @RequestParam("startDate") Timestamp startDate,
            @RequestParam("endDate") Timestamp endDate,
            @RequestParam("totalDonations") double totalDonations) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Donations volunteer = donationService.createVolunteer(title, name, description, coverImage, sponsorImages, startDate, endDate, totalDonations, username);
        return ResponseEntity.ok(volunteer);
    }

    @PutMapping(value = "/admin/donate/update-donation/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Donations> updateDonation(
            @PathVariable Long id,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestPart(value = "coverImage", required = false) List<MultipartFile> coverImage,
            @RequestPart(value = "sponsorImages", required = false) List<MultipartFile> sponsorImages,
            @RequestParam(value = "startDate", required = false) Timestamp startDate,
            @RequestParam(value = "endDate", required = false) Timestamp endDate,
            @RequestParam(value = "totalDonations", required = false) Double totalDonations) throws IOException {
        Donations updatedDonation = donationService.updateDonation(id, title, name, description, coverImage, sponsorImages, startDate, endDate, totalDonations != null ? totalDonations : 0.0);
        return ResponseEntity.ok(updatedDonation);
    }

    @DeleteMapping("/admin/donate/delete-donation-by-id")
    public ResponseEntity<String> deleteDonation(@RequestParam Long donationId) {
        try {
            donationRepository.deleteById(donationId);
            return ResponseEntity.ok("Donation deleted successfully");
        } catch (Exception e) {
            logger.error("Error deleting donation with id {}: {}", donationId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete donation: " + e.getMessage());
        }
    }
}
