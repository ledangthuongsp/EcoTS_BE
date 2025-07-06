package com.example.EcoTS.Services.DonationService;

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
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DonationService {

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private SponsorRepository sponsorRepository;        // ← thêm repository cho Sponsor

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


    /**
     * Sponsor tạo mới một Donation
     *
     * @param title           tiêu đề
     * @param name            tên hiển thị
     * @param description     mô tả
     * @param coverImages     ảnh cover
     * @param sponsorImages   ảnh tài trợ thêm
     * @param startDate       thời gian bắt đầu
     * @param endDate         thời gian kết thúc
     * @param sponsorUsername username của Sponsor
     * @return Donation đã lưu
     */
    @Transactional
    public Donations createDonation(
            String title,
            String name,
            String description,
            List<MultipartFile> coverImages,
            List<MultipartFile> sponsorImages,
            Timestamp startDate,
            Timestamp endDate,
            String sponsorUsername
    ) throws IOException {
        // 1. Lấy Sponsor từ DB
        Sponsor sponsor = sponsorRepository
                .findByCompanyUsername(sponsorUsername);

        // 2. Upload ảnh lên Cloudinary
        List<String> coverUrls = cloudinaryService.uploadMultipleFilesDonations(coverImages);
        List<String> sponsorUrls = cloudinaryService.uploadMultipleFilesDonations(sponsorImages);

        // 3. Build Donations entity (khởi tạo totalDonations = 0)
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

        // 4. Save donation
        Donations saved = donationRepository.save(donation);

        // 5. Cập nhật quan hệ 2 chiều (nếu cần)
        sponsor.getDonations().add(saved);
        sponsorRepository.save(sponsor);

        // 6. Tạo notification
        Notifications notification = Notifications.builder()
                .title("New Donation Created")
                .description("A new donation has been created: " + title)
                .build();
        notificationRepository.save(notification);

        // 7. Gửi notification đến tất cả người dùng
        notificationService.notifyAllUsers(
                notification.getTitle(),
                notification.getDescription(),
                sponsorUsername
        );

        return saved;
    }


    @Transactional
    public Donations updateDonation(
            Long id,
            String title,
            String name,
            String description,
            List<MultipartFile> coverImages,
            List<MultipartFile> sponsorImages,
            Timestamp startDate,
            Timestamp endDate,
            Double totalDonations
    ) throws IOException {
        Donations donation = donationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donation không tồn tại"));

        if (coverImages != null && !coverImages.isEmpty()) {
            donation.setCoverImageUrl(
                    cloudinaryService.uploadMultipleFilesDonations(coverImages));
        }
        if (sponsorImages != null && !sponsorImages.isEmpty()) {
            donation.setSponsorImages(
                    cloudinaryService.uploadMultipleFilesDonations(sponsorImages));
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
        if (totalDonations != null) {
            donation.setTotalDonations(totalDonations);
        }

        return donationRepository.save(donation);
    }


    @Transactional
    public void donatePoints(String username, Long donationId, double donationPoints) {
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User không tồn tại"));

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
                .orElseThrow(() -> new IllegalArgumentException("Donation không tồn tại"));
        donation.setTotalDonations(donation.getTotalDonations() + donationPoints);
        donationRepository.save(donation);

        // Lưu lịch sử
        DonationHistory history = DonationHistory.builder()
                .userId(user.getId())
                .points(donationPoints)
                .donationLocation(donation.getTitle())
                .build();
        donationHistoryRepository.save(history);
    }


    public List<Donations> getAllDonation() {
        return donationRepository.findAll();
    }

    public Donations getDonationById(Long id) {
        return donationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Donation không tồn tại"));
    }

    public List<Donations> getPastDonations() {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        return donationRepository.findAll().stream()
                .filter(d -> d.getEndDate().before(now))
                .toList();
    }

    public List<Donations> getOngoingDonations() {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        return donationRepository.findAll().stream()
                .filter(d -> d.getStartDate().before(now) && d.getEndDate().after(now))
                .toList();
    }

    public List<Donations> getUpcomingDonations() {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        return donationRepository.findAll().stream()
                .filter(d -> d.getStartDate().after(now))
                .toList();
    }
}
