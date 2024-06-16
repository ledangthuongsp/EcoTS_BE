package com.example.EcoTS.Services.DonationService;

import com.example.EcoTS.Models.*;
import com.example.EcoTS.Repositories.*;
import com.example.EcoTS.Services.CloudinaryService.CloudinaryService;

import com.example.EcoTS.Services.Notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
public class DonationService {
    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private PointRepository pointRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DonationHistoryRepository donationHistoryRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private NotificationRepository notificationRepository;

    public Donations createVolunteer(String title, String name, String description, List<MultipartFile> coverImage, List<MultipartFile> sponsorImages, LocalDate startDate, LocalDate endDate, double totalDonations) throws IOException {
        List<String> coverImageUrl = cloudinaryService.uploadMultipleFilesDonations(coverImage);
        List<String> sponsorImageUrls = cloudinaryService.uploadMultipleFilesDonations(sponsorImages);

        Donations volunteer = new Donations();
        volunteer.setName(name);
        volunteer.setTitle(title);
        volunteer.setDescription(description);
        volunteer.setCoverImageUrl(coverImageUrl);
        volunteer.setSponsorImages(sponsorImageUrls);
        volunteer.setStartDate(startDate);
        volunteer.setEndDate(endDate);
        volunteer.setTotalDonations(totalDonations);

        Donations savedVolunteer = donationRepository.save(volunteer);

        // Send notification
        // Create and save notification
        Notifications notification = Notifications.builder()
                .title("New Donation Created")
                .description("A new donation has been created: " + title)
                .build();
        notificationRepository.save(notification);
        // Notify all users
        notificationService.notifyAllUsers(notification.getTitle(), notification.getDescription());

        return savedVolunteer;
    }

    public void donatePoints(String username, Long donationId, double donationPoints) {
        // Find user by username
        Users user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Find user points by userId
        Points userPoints = pointRepository.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("User points not found"));

        // Check if the user has enough points to donate
        if (userPoints.getPoint() < donationPoints) {
            throw new IllegalArgumentException("Insufficient points");
        }

        // Update user's points
        userPoints.setPoint(userPoints.getPoint() - donationPoints);
        pointRepository.save(userPoints);

        // Find donation to update total points
        Donations donation = donationRepository.findById(donationId).orElseThrow(() -> new IllegalArgumentException("Donation not found"));

        // Update total points of the donation
        donation.setTotalDonations(donation.getTotalDonations() + donationPoints);
        donationRepository.save(donation);

        // Create and save donation history
        DonationHistory donationHistory = DonationHistory.builder()
                .userId(user.getId())
                .points(donationPoints)
                .donationLocation(donation.getTitle())
                .build();
        donationHistoryRepository.save(donationHistory);
    }

    public List<Donations> getAllDonation() {
        return donationRepository.findAll();
    }

    public Donations getDonationById(Long id) {
        return donationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Donation not found"));
    }
}
