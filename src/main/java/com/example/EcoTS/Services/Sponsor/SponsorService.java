package com.example.EcoTS.Services.Sponsor;


import com.example.EcoTS.DTOs.Response.Sponsor.SponsorResponse;
import com.example.EcoTS.Mappers.SponsorMapper;
import com.example.EcoTS.Models.Sponsor;
import com.example.EcoTS.Models.SponsorQRCode;
import com.example.EcoTS.Models.Users;
import com.example.EcoTS.Repositories.SponsorQRCodeRepository;
import com.example.EcoTS.Repositories.SponsorRepository;
import com.example.EcoTS.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    // Tạo mới sponsor
    @Transactional
    public SponsorResponse createSponsor(Sponsor sponsor) {
        Sponsor saved = sponsorRepository.save(sponsor);
        return sponsorMapper.toDTO(saved);
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
