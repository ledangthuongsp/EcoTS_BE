package com.example.EcoTS.Repositories;

import com.example.EcoTS.Models.SponsorQRCode;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Hidden
public interface SponsorQRCodeRepository extends JpaRepository<SponsorQRCode, Long> {
    List<SponsorQRCode> findBySponsorIdAndIsUsedFalse(Long sponsorId);  // Tìm QR code chưa sử dụng cho sponsor
    Optional<SponsorQRCode> findByIdAndIsUsedFalse(Long id);
}
