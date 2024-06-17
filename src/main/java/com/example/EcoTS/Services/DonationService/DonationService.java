package com.example.EcoTS.Services.DonationService;

import com.example.EcoTS.Enum.Roles;
import com.example.EcoTS.Models.*;
import com.example.EcoTS.Repositories.*;
import com.example.EcoTS.Services.CloudinaryService.CloudinaryService;

import com.example.EcoTS.Services.Notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


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
    @Autowired
    private ResultRepository resultRepository;

    @Transactional
    public Donations createVolunteer(String title, String name, String description, List<MultipartFile> coverImage, List<MultipartFile> sponsorImages, Timestamp startDate, Timestamp endDate, double totalDonations, String username) throws IOException {
        Users user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!Roles.EMPLOYEE.name().equals(user.getRole())) {
            throw new IllegalArgumentException("Only employees can create donations");
        }

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
        Notifications notification = Notifications.builder()
                .title("New Donation Created")
                .description("A new donation has been created: " + title)
                .build();
        notificationRepository.save(notification);

        // Notify all users
        notificationService.notifyAllUsers(notification.getTitle(), notification.getDescription(), username);

        return savedVolunteer;
    }
    @Transactional
    public Donations updateDonation(Long id, String title, String name, String description, List<MultipartFile> coverImage,
                                    List<MultipartFile> sponsorImages, Timestamp startDate, Timestamp endDate,
                                    double totalDonations) throws IOException {
        Donations donation = donationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Donation not found"));

        if (coverImage != null && !coverImage.isEmpty()) {
            List<String> coverImageUrl = cloudinaryService.uploadMultipleFilesDonations(coverImage);
            donation.setCoverImageUrl(coverImageUrl);
        }

        if (sponsorImages != null && !sponsorImages.isEmpty()) {
            List<String> sponsorImageUrls = cloudinaryService.uploadMultipleFilesDonations(sponsorImages);
            donation.setSponsorImages(sponsorImageUrls);
        }

        if (title != null && !title.isEmpty()) {
            donation.setTitle(title);
        }

        if (name != null && !name.isEmpty()) {
            donation.setName(name);
        }

        if (description != null && !description.isEmpty()) {
            donation.setDescription(description);
        }

        if (startDate != null) {
            donation.setStartDate(startDate);
        }

        if (endDate != null) {
            donation.setEndDate(endDate);
        }

        donation.setTotalDonations(totalDonations);

        return donationRepository.save(donation);
    }

    @Transactional
    public void donatePoints(String username, Long donationId, double donationPoints) {
        // Find user by username
        Users user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Find user points by userId
        Points userPoints = pointRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("User points not found"));

        // Check if the user has enough points to donate
        if (userPoints.getPoint() < donationPoints) {
            throw new IllegalArgumentException("Insufficient points");
        }

        // Update user's points
        userPoints.setPoint(userPoints.getPoint() - donationPoints);
        pointRepository.save(userPoints);

        // Update result achievement
        Results results = resultRepository.findByUser(user).orElseThrow(() -> new IllegalArgumentException("User not found"));
        //so lan donate
        results.setNumberOfTimeDonate(results.getNumberOfTimeDonate() + 1);
        //so diem da donate
        results.setPointDonate(results.getPointDonate() + donationPoints);
        resultRepository.save(results);
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
    public List<Donations> getPastDonations() {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        return donationRepository.findAll()
                .stream()
                .filter(donation -> donation.getEndDate().before(now))
                .collect(Collectors.toList());
    }

    public List<Donations> getOngoingDonations() {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        return donationRepository.findAll()
                .stream()
                .filter(donation -> donation.getStartDate().before(now) && donation.getEndDate().after(now))
                .collect(Collectors.toList());
    }

    public List<Donations> getUpcomingDonations() {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        return donationRepository.findAll()
                .stream()
                .filter(donation -> donation.getStartDate().after(now))
                .collect(Collectors.toList());
    }
}
