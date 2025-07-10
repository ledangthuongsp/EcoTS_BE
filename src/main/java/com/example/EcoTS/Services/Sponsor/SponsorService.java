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
import java.util.List;
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

            // Kiểm tra xem email đã tồn tại trong SponsorCreate hoặc Sponsor table hay không
            if (isEmailAlreadyExists(sponsorCreate.getEmail())) {
                // Nếu email đã tồn tại, xóa bản ghi sponsorCreate đang Pending
                sponsorCreateRepository.delete(sponsorCreate);

                // Gửi email thông báo lỗi đến người dùng
                emailService.sendEmail(
                        sponsorCreate.getEmail(),
                        "Email already exists",
                        "We are sorry, but the email you provided already has an account in our system. Please check again later."
                );

                // Ném ngoại lệ để dừng quá trình xử lý nếu email trùng
                throw new RuntimeException("Email already exists. Please use a different email address.");
            }

            // Nếu không trùng email, tiếp tục xác nhận sponsor và tạo tài khoản mới
            Sponsor sponsor = new Sponsor();
            sponsor.setCompanyUsername(sponsorCreate.getCompanyName());
            sponsor.setCompanyPassword(generateRandomPassword());  // Tạo mật khẩu ngẫu nhiên
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

            // Cập nhật trạng thái sponsorCreate thành CONFIRMED
            sponsorCreate.setStatus(SponsorCreate.Status.CONFIRMED);
            sponsorCreateRepository.save(sponsorCreate);

            // Gửi mật khẩu tạm thời qua email cho sponsor
            emailService.sendTemporaryPassword(sponsorCreate.getEmail(), sponsor.getCompanyPassword());
        }
    }

    // Method to check if the email already exists in either the SponsorCreate or Sponsor table
    private boolean isEmailAlreadyExists(String email) {
        // Check in SponsorCreate
        // Check in Sponsor
        if (sponsorRepository.findByCompanyEmailContact(email) != null) {
            return true;
        }
        return false;
    }

    public boolean isFirstTimeLogin(Long sponsorId) {
        Sponsor sponsor = sponsorRepository.findById(sponsorId)
                .orElseThrow(() -> new RuntimeException("Sponsor not found"));
        return sponsor.isFirstLogin();
    }

    // Cập nhật mật khẩu khi sponsor đổi mật khẩu lần đầu
    @Transactional
    public void changePassword(Long sponsorId, String oldPassword, String newPassword) {
        Sponsor sponsor = sponsorRepository.findById(sponsorId)
                .orElseThrow(() -> new RuntimeException("Sponsor not found"));

        // Kiểm tra mật khẩu cũ
        if (!sponsor.getCompanyPassword().equals(oldPassword)) {
            throw new RuntimeException("Old password is incorrect");
        }

        // Cập nhật mật khẩu mới
        sponsor.setCompanyPassword(newPassword);
        sponsorRepository.save(sponsor);
    }

    // Cập nhật thông tin sponsor
    @Transactional
    public SponsorResponse updateSponsor(Long id, Sponsor sponsorDetails) {
        Sponsor sponsor = sponsorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sponsor không tồn tại với id: " + id));

        // Cập nhật các trường, nhưng không thay đổi email, password và points
        if (sponsorDetails.getCompanyUsername() != null) {
            sponsor.setCompanyUsername(sponsorDetails.getCompanyUsername());
        }
        if (sponsorDetails.getAvatarUrl() != null) {
            sponsor.setAvatarUrl(sponsorDetails.getAvatarUrl());
        }
        if (sponsorDetails.getCompanyName() != null) {
            sponsor.setCompanyName(sponsorDetails.getCompanyName());
        }
        if (sponsorDetails.getCompanyPhoneNumberContact() != null) {
            sponsor.setCompanyPhoneNumberContact(sponsorDetails.getCompanyPhoneNumberContact());
        }
        if (sponsorDetails.getCompanyAddress() != null) {
            sponsor.setCompanyAddress(sponsorDetails.getCompanyAddress());
        }
        if (sponsorDetails.getBusinessDescription() != null) {
            sponsor.setBusinessDescription(sponsorDetails.getBusinessDescription());
        }
        if (sponsorDetails.getCompanyDirectorName() != null) {
            sponsor.setCompanyDirectorName(sponsorDetails.getCompanyDirectorName());
        }
        if (sponsorDetails.getCompanyTaxNumber() != null) {
            sponsor.setCompanyTaxNumber(sponsorDetails.getCompanyTaxNumber());
        }

        // Không thay đổi password, email và points
        // Do đó, chúng không được cập nhật trong khi save

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

    public List<SponsorCreate> getAllPendingSponsors() {
        return sponsorCreateRepository.findByStatus(SponsorCreate.Status.PENDING);
    }
}
