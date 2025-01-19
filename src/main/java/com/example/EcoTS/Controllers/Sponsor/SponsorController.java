package com.example.EcoTS.Controllers.Sponsor;

import com.example.EcoTS.Models.Sponsor;
import com.example.EcoTS.Models.SponsorQRCode;
import com.example.EcoTS.Services.CloudinaryService.CloudinaryService;
import com.example.EcoTS.Services.Sponsor.SponsorQRCodeService;
import com.example.EcoTS.Services.Sponsor.SponsorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/sponsor")
@CrossOrigin
@Tag(name = "API For Sponsor")
public class SponsorController {

    @Autowired
    private SponsorService sponsorService;
    @Autowired
    private SponsorQRCodeService sponsorQRCodeService;
    @Autowired
    private CloudinaryService cloudinaryService;

    // API tạo mới sponsor
    @PostMapping("/create")
    public ResponseEntity<Sponsor> createSponsor(@RequestBody Sponsor sponsor) {
        Sponsor createdSponsor = sponsorService.createSponsor(sponsor);
        return ResponseEntity.ok(createdSponsor);
    }

    // API cập nhật sponsor
    @PutMapping("/update/{id}")
    public ResponseEntity<Sponsor> updateSponsor(@PathVariable Long id, @RequestBody Sponsor sponsorDetails) {
        Sponsor updatedSponsor = sponsorService.updateSponsor(id, sponsorDetails);
        return ResponseEntity.ok(updatedSponsor);
    }

    // API lấy thông tin sponsor theo id
    @GetMapping("/{id}")
    public ResponseEntity<Sponsor> getSponsorById(@PathVariable Long id) {
        Sponsor sponsor = sponsorService.getSponsorById(id);
        return ResponseEntity.ok(sponsor);
    }

    // API xóa sponsor
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteSponsor(@PathVariable Long id) {
        sponsorService.deleteSponsor(id);
        return ResponseEntity.noContent().build();
    }
    // API lấy QR Code còn hiệu lực cho sponsor
    @GetMapping("/qrcode/active/{sponsorId}")
    public ResponseEntity<?> getActiveQRCode(@PathVariable Long sponsorId) {
        try {
            Optional<SponsorQRCode> qrCode = sponsorQRCodeService.getActiveQRCode(sponsorId);
            return qrCode.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            // Tùy chỉnh thông báo lỗi để trả về client
            String errorMessage = "Không thể lấy QR code còn hiệu lực cho sponsorId: " + sponsorId;

            // Trả về lỗi với thông điệp tùy chỉnh và mã trạng thái 500
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorMessage);
        }
    }

    // API tạo QR Code mới
    @PostMapping("/qrcode/generate")
    public ResponseEntity<?> generateQRCode(@RequestParam Long sponsorId, @RequestParam Double points, @RequestParam Long newsfeedId) {
        try {
            SponsorQRCode qrCode = sponsorQRCodeService.generateQRCode(sponsorId, points, newsfeedId);
            return ResponseEntity.ok(qrCode);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error generating QR Code");
        }
    }

    // API sử dụng QR Code
    @PostMapping(value = "/qrcode/use", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> useQRCode(@RequestParam Long qrCodeId, @RequestParam String userEmail, @RequestPart MultipartFile proofImage) throws IOException {
        String proofImageUrl = cloudinaryService.uploadUserProofImage(proofImage);
        SponsorQRCode qrCode = sponsorQRCodeService.useQRCode(qrCodeId, userEmail, proofImageUrl);
        if (qrCode != null) {
            return ResponseEntity.ok(qrCode);
        }
        return ResponseEntity.notFound().build();
    }

    // API làm mới QR Code
    @PostMapping("/qrcode/refresh")
    public ResponseEntity<?> refreshQRCode(@RequestParam Long sponsorId, @RequestParam Long qrCodeId) {
        try {
            SponsorQRCode qrCode = sponsorQRCodeService.refreshQRCode(sponsorId, qrCodeId);
            if (qrCode != null) {
                return ResponseEntity.ok(qrCode);
            }
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error refreshing QR Code");
        }
    }

}
