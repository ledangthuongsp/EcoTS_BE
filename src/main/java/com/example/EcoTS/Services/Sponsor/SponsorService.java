package com.example.EcoTS.Services.Sponsor;


import com.example.EcoTS.DTOs.Response.Sponsor.SponsorResponse;
import com.example.EcoTS.Mappers.SponsorMapper;
import com.example.EcoTS.Models.Sponsor;
import com.example.EcoTS.Models.SponsorCreate;
import com.example.EcoTS.Models.SponsorQRCode;
import com.example.EcoTS.Models.Users;
import com.example.EcoTS.Repositories.SponsorCreateRepository;
import com.example.EcoTS.Repositories.SponsorQRCodeRepository;
import com.example.EcoTS.Repositories.SponsorRepository;
import com.example.EcoTS.Repositories.UserRepository;
import com.example.EcoTS.Services.Email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class SponsorService {


    @Autowired
    private SponsorRepository sponsorRepository;

    @Autowired
    private SponsorMapper sponsorMapper;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SponsorCreateRepository sponsorCreateRepository;

    private String generateRandomPassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(8);

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(characters.length());
            password.append(characters.charAt(index));
        }

        return password.toString();
    }

    @Transactional
    public SponsorCreate createSponsor(SponsorCreate sponsorCreate) {
        if (sponsorCreate.getEmail() == null || sponsorCreate.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        sponsorCreate.setStatus(SponsorCreate.Status.PENDING);
        return sponsorCreateRepository.save(sponsorCreate);
    }

    @Transactional
    public void confirmSponsor(Long sponsorCreateId) {
        Optional<SponsorCreate> sponsorCreateOpt = sponsorCreateRepository.findById(sponsorCreateId);

        if (sponsorCreateOpt.isPresent()) {
            SponsorCreate sponsorCreate = sponsorCreateOpt.get();

            // Chuyển thông tin từ bảng tạm vào bảng sponsor
            Sponsor sponsor = new Sponsor();
            sponsor.setCompanyUsername(sponsorCreate.getCompanyName());
            sponsor.setCompanyPassword(generateRandomPassword());  // Mật khẩu ngẫu nhiên
            sponsor.setAvatarUrl("");  // Tạm thời chưa có avatar
            sponsor.setCompanyName(sponsorCreate.getCompanyName());
            sponsor.setCompanyPhoneNumberContact(sponsorCreate.getContactPhone());
            sponsor.setCompanyEmailContact(sponsorCreate.getEmail());
            sponsor.setCompanyAddress(sponsorCreate.getAddress());
            sponsor.setBusinessDescription(sponsorCreate.getIdea());
            sponsor.setCompanyDirectorName("");  // Tạm thời để trống
            sponsor.setCompanyTaxNumber(sponsorCreate.getTaxNumber());
            sponsor.setCompanyPoints(0.0);  // Khởi tạo điểm sponsor

            // Lưu sponsor vào bảng sponsor
            sponsorRepository.save(sponsor);

            // Cập nhật trạng thái sponsorCreate là CONFIRMED
            sponsorCreate.setStatus(SponsorCreate.Status.CONFIRMED);
            sponsorCreateRepository.save(sponsorCreate);

            // Gửi mật khẩu tạm thời qua email cho sponsor
            emailService.sendTemporaryPassword(sponsorCreate.getEmail(), sponsor.getCompanyPassword());
        }
    }
    public boolean isFirstTimeLogin(Long sponsorId) {
        Sponsor sponsor = sponsorRepository.findById(sponsorId)
                .orElseThrow(() -> new RuntimeException("Sponsor not found"));
        return sponsor.isFirstLogin();
    }

    // Cập nhật mật khẩu khi sponsor đổi mật khẩu lần đầu
    @Transactional
    public void changePassword(Long sponsorId, String newPassword) {
        Sponsor sponsor = sponsorRepository.findById(sponsorId)
                .orElseThrow(() -> new RuntimeException("Sponsor not found"));

        // Đổi mật khẩu và đánh dấu không phải lần đăng nhập đầu tiên nữa
        sponsor.setCompanyPassword(newPassword);  // Mã hóa mật khẩu trước khi lưu
        sponsor.setFirstLogin(false);  // Đánh dấu sponsor đã đăng nhập lần đầu
        sponsorRepository.save(sponsor);
    }

    // Cập nhật thông tin sponsor
    @Transactional
    public SponsorResponse updateSponsor(Long id, Sponsor sponsorDetails) {
        Sponsor sponsor = sponsorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sponsor không tồn tại với id: " + id));

        // Cập nhật các trường (không động chạm đến quan hệ locations/donations)
        sponsor.setCompanyUsername(sponsorDetails.getCompanyUsername());
        sponsor.setCompanyPassword(sponsorDetails.getCompanyPassword());
        sponsor.setAvatarUrl(sponsorDetails.getAvatarUrl());
        sponsor.setCompanyName(sponsorDetails.getCompanyName());
        sponsor.setCompanyPhoneNumberContact(sponsorDetails.getCompanyPhoneNumberContact());
        sponsor.setCompanyEmailContact(sponsorDetails.getCompanyEmailContact());
        sponsor.setCompanyAddress(sponsorDetails.getCompanyAddress());
        sponsor.setBusinessDescription(sponsorDetails.getBusinessDescription());
        sponsor.setCompanyDirectorName(sponsorDetails.getCompanyDirectorName());
        sponsor.setCompanyTaxNumber(sponsorDetails.getCompanyTaxNumber());
        sponsor.setCompanyPoints(sponsorDetails.getCompanyPoints());

        Sponsor updated = sponsorRepository.save(sponsor);
        return sponsorMapper.toDTO(updated);
    }

    // Lấy thông tin sponsor theo id
    public SponsorResponse getSponsorById(Long id) {
        Sponsor sponsor = sponsorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sponsor không tồn tại với id: " + id));
        return sponsorMapper.toDTO(sponsor);
    }


    // Xóa sponsor
    @Transactional
    public void deleteSponsor(Long id) {
        sponsorRepository.deleteById(id);
    }
}
