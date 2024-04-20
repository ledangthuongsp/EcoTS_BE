package com.example.EcoTS.Services.DonationService;

import com.example.EcoTS.Models.Donations;
import com.example.EcoTS.Repositories.DonationRepository;
import com.example.EcoTS.Services.CloudinaryService.CloudinaryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class DonationService {
    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

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
}
