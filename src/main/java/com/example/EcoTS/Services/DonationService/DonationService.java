package com.example.EcoTS.Services.DonationService;

import com.example.EcoTS.DTOs.Response.Donation.DonationResponseDTO;
import com.example.EcoTS.Mappers.DonationMapper;
import com.example.EcoTS.Models.*;
import com.example.EcoTS.Repositories.*;
import com.example.EcoTS.Services.CloudinaryService.CloudinaryService;
import com.example.EcoTS.Services.Notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DonationService {

    private final DonationRepository donationRepository;
    private final SponsorRepository sponsorRepository;
    private final CloudinaryService cloudinaryService;
    private final NotificationService notificationService;
    private final NotificationRepository notificationRepository;
    private final PointRepository pointRepository;
    private final UserRepository userRepository;
    private final ResultRepository resultRepository;
    private final DonationHistoryRepository donationHistoryRepository;
    private final DonationMapper donationMapper;

    /**
     * Sponsor tạo mới một Donation
     */
    @Transactional
    public DonationResponseDTO createDonation(
            Long sponsorId,
            String title,
            String name,
            String description,
            List<MultipartFile> coverImages,
            List<MultipartFile> sponsorImages,
            Timestamp startDate,
            Timestamp endDate
    ) throws IOException {
        Sponsor sponsor = sponsorRepository.findById(sponsorId)
                .orElseThrow(() -> new ResourceNotFoundException("Sponsor không tồn tại: " + sponsorId));

        List<String> coverUrls   = cloudinaryService.uploadMultipleFilesDonations(coverImages);
        List<String> sponsorUrls = cloudinaryService.uploadMultipleFilesDonations(sponsorImages);

        Donations donation = Donations.builder()
                .title(title)
                .name(name)
                .description(description)
                .coverImageUrl(coverUrls)
                .sponsorImages(sponsorUrls)
                .startDate(startDate)
                .endDate(endDate)
                .totalDonations(0.0)
                .sponsor(sponsor)
                .build();

        Donations saved = donationRepository.save(donation);

        // Cập nhật quan hệ hai chiều (nếu cần)
        sponsor.getDonations().add(saved);
        sponsorRepository.save(sponsor);

        // Tạo và gửi notification
        Notifications notif = Notifications.builder()
                .title("New Donation Created")
                .description("A new donation has been created: " + title)
                .build();
        notificationRepository.save(notif);
        notificationService.notifyAllUsers(
                notif.getTitle(),
                notif.getDescription(),
                sponsor.getCompanyUsername()
        );

        return donationMapper.toDTO(saved);
    }

    /**
     * Cập nhật Donation
     */
    @Transactional
    public DonationResponseDTO updateDonation(
            Long donationId,
            String title,
            String name,
            String description,
            List<MultipartFile> coverImages,
            List<MultipartFile> sponsorImages,
            Timestamp startDate,
            Timestamp endDate,
            Double totalDonations
    ) throws IOException {
        Donations donation = donationRepository.findById(donationId)
                .orElseThrow(() -> new ResourceNotFoundException("Donation không tồn tại: " + donationId));

        if (coverImages != null && !coverImages.isEmpty()) {
            donation.setCoverImageUrl(cloudinaryService.uploadMultipleFilesDonations(coverImages));
        }
        if (sponsorImages != null && !sponsorImages.isEmpty()) {
            donation.setSponsorImages(cloudinaryService.uploadMultipleFilesDonations(sponsorImages));
        }
        if (title           != null) donation.setTitle(title);
        if (name            != null) donation.setName(name);
        if (description     != null) donation.setDescription(description);
        if (startDate       != null) donation.setStartDate(startDate);
        if (endDate         != null) donation.setEndDate(endDate);
        if (totalDonations  != null) donation.setTotalDonations(totalDonations);

        Donations updated = donationRepository.save(donation);
        return donationMapper.toDTO(updated);
    }

    /**
     * Người dùng donate points vào một Donation
     */
    @Transactional
    public void donatePoints(String username, Long donationId, double donationPoints) {
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User không tồn tại: " + username));

        Points userPoints = pointRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("User points không tồn tại"));

        if (userPoints.getPoint() < donationPoints) {
            throw new IllegalArgumentException("Insufficient points");
        }

        // Trừ điểm user
        userPoints.setPoint(userPoints.getPoint() - donationPoints);
        pointRepository.save(userPoints);

        // Cập nhật kết quả user
        Results results = resultRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Results không tồn tại cho user này"));
        results.setNumberOfTimeDonate(results.getNumberOfTimeDonate() + 1);
        results.setPointDonate(results.getPointDonate() + donationPoints);
        resultRepository.save(results);

        // Cập nhật tổng donation
        Donations donation = donationRepository.findById(donationId)
                .orElseThrow(() -> new IllegalArgumentException("Donation không tồn tại: " + donationId));
        donation.setTotalDonations(donation.getTotalDonations() + donationPoints);
        donationRepository.save(donation);

        // Lưu lịch sử donate
        DonationHistory history = DonationHistory.builder()
                .userId(user.getId())
                .points(donationPoints)
                .donationLocation(donation.getTitle())
                .build();
        donationHistoryRepository.save(history);
    }

    /**
     * Lấy tất cả Donation ở dạng DTO
     */
    public List<DonationResponseDTO> getAllDonations() {
        return donationRepository.findAll().stream()
                .map(donationMapper::toDTO)
                .toList();
    }

    /**
     * Lấy Donation theo ID
     */
    public DonationResponseDTO getDonationById(Long id) {
        Donations donation = donationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donation không tồn tại: " + id));
        return donationMapper.toDTO(donation);
    }

    /**
     * Lấy tất cả Donation của một Sponsor
     */
    public List<DonationResponseDTO> getDonationsBySponsorId(Long sponsorId) {
        sponsorRepository.findById(sponsorId)
                .orElseThrow(() -> new ResourceNotFoundException("Sponsor không tồn tại: " + sponsorId));
        return donationRepository.findBySponsorId(sponsorId).stream()
                .map(donationMapper::toDTO)
                .toList();
    }

    /**
     * Lấy các Donation đã kết thúc
     */
    public List<DonationResponseDTO> getPastDonations() {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        return donationRepository.findAll().stream()
                .filter(d -> d.getEndDate().before(now))
                .map(donationMapper::toDTO)
                .toList();
    }

    /**
     * Lấy các Donation đang diễn ra
     */
    public List<DonationResponseDTO> getOngoingDonations() {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        return donationRepository.findAll().stream()
                .filter(d -> d.getStartDate().before(now) && d.getEndDate().after(now))
                .map(donationMapper::toDTO)
                .toList();
    }

    /**
     * Lấy các Donation sắp diễn ra
     */
    public List<DonationResponseDTO> getUpcomingDonations() {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        return donationRepository.findAll().stream()
                .filter(d -> d.getStartDate().after(now))
                .map(donationMapper::toDTO)
                .toList();
    }
}
