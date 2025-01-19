package com.example.EcoTS.Repositories;

import com.example.EcoTS.Models.QRCodeUsage;
import com.example.EcoTS.Models.SponsorQRCode;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Hidden
public interface QRCodeUsageRepository extends JpaRepository<QRCodeUsage, Long> {
    boolean existsBySponsorQRCodeAndUserEmail(SponsorQRCode sponsorQRCode, String userEmail);
}
