package com.example.EcoTS.Services.DonationService;

import com.example.EcoTS.Models.Donations;
import com.example.EcoTS.Models.Points;
import com.example.EcoTS.Models.Users;
import com.example.EcoTS.Repositories.DonationRepository;
import com.example.EcoTS.Repositories.PointRepository;
import com.example.EcoTS.Repositories.UserRepository;
import com.example.EcoTS.Services.CloudinaryService.CloudinaryService;
import com.google.zxing.NotFoundException;

import net.bytebuddy.asm.Advice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import jakarta.transaction.Transactional;

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
    public Donations createVolunteer(String title, String description, List<MultipartFile> coverImage, List<MultipartFile> sponsorImages, LocalDate startDate, LocalDate endDate, double totalDonations) throws IOException, IOException {
        List<String> coverImageUrl = cloudinaryService.uploadMultipleFilesDonations(coverImage);
        List<String> sponsorImageUrls = cloudinaryService.uploadMultipleFilesDonations(sponsorImages);

        Donations volunteer = new Donations();
        volunteer.setTitle(title);
        volunteer.setDescription(description);
        volunteer.setCoverImageUrl(coverImageUrl);
        volunteer.setSponsorImages(sponsorImageUrls);
        volunteer.setStartDate(startDate);
        volunteer.setEndDate(endDate);
        volunteer.setTotalDonations(totalDonations);

        return donationRepository.save(volunteer);
    }
    public void donatePoints(String username, Long donationId, double donationPoints) {
        // Tìm userId từ username
        Users user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Tìm điểm của người dùng từ userId
        Points userPoints = pointRepository.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("User points not found"));

        // Kiểm tra xem người dùng có đủ điểm để donate không
        if (userPoints.getPoint() < donationPoints) {
            throw new IllegalArgumentException("Insufficient points");
        }

        // Cập nhật điểm của người dùng
        userPoints.setPoint(userPoints.getPoint() - donationPoints);
        pointRepository.save(userPoints);

        // Tìm donation để cập nhật tổng điểm
        Donations donation = donationRepository.findById(donationId).orElseThrow(() -> new IllegalArgumentException("Donation not found"));

        // Cập nhật tổng điểm của donation
        donation.setTotalDonations(donation.getTotalDonations() + donationPoints);
        donationRepository.save(donation);
    }
    public List<Donations> getAllDonation (){
        return donationRepository.findAll();
    }
}
