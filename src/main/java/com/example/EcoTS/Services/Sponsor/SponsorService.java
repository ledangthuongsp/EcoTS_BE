package com.example.EcoTS.Services.Sponsor;


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
    // Tạo mới sponsor
    @Transactional
    public Sponsor createSponsor(Sponsor sponsor) {
        return sponsorRepository.save(sponsor);
    }

    // Cập nhật thông tin sponsor
    @Transactional
    public Sponsor updateSponsor(Long id, Sponsor sponsorDetails) {
        Optional<Sponsor> sponsorOptional = sponsorRepository.findById(id);
        if (sponsorOptional.isPresent()) {
            Sponsor sponsor = sponsorOptional.get();
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

            return sponsorRepository.save(sponsor); // Lưu lại sponsor sau khi cập nhật
        } else {
            throw new RuntimeException("Sponsor không tồn tại với id: " + id);
        }
    }

    // Lấy thông tin sponsor theo id
    public Sponsor getSponsorById(Long id) {
        return sponsorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sponsor không tồn tại với id: " + id));
    }

    // Xóa sponsor
    @Transactional
    public void deleteSponsor(Long id) {
        sponsorRepository.deleteById(id);
    }
}
