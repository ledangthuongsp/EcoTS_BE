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
import java.sql.Timestamp;
import java.util.Collections;
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
            if (qrCode.isPresent()) {
                SponsorQRCode activeQrCode = qrCode.get();

                // Kiểm tra nếu mã đã hết hạn
                if (activeQrCode.getExpiredAt().before(new Timestamp(System.currentTimeMillis()))) {
                    return ResponseEntity.ok(Collections.singletonMap("message", "QR Code đã hết hạn."));
                }

                // Trả về QR code hợp lệ
                return ResponseEntity.ok(activeQrCode);
            } else {
                String notFoundMessage = "Không tìm thấy QR code còn hiệu lực cho sponsorId: " + sponsorId;
                return ResponseEntity.ok(Collections.singletonMap("message", notFoundMessage));
            }
        } catch (Exception e) {
            String errorMessage = "Không thể lấy QR code còn hiệu lực cho sponsorId: " + sponsorId;
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", errorMessage));
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
    public ResponseEntity<?> useQRCode(@RequestParam Long qrCodeId, @RequestParam String userEmail, @RequestPart MultipartFile proofImage) {
        try {
            String proofImageUrl = cloudinaryService.uploadUserProofImage(proofImage);

            // Gọi service để xử lý sử dụng QR code
            SponsorQRCode qrCode = sponsorQRCodeService.useQRCode(qrCodeId, userEmail, proofImageUrl);

            // Nếu sử dụng thành công, trả về thông tin QR code
            if (qrCode != null) {
                return ResponseEntity.ok(Collections.singletonMap("message", "QR Code được sử dụng thành công."));
            }

            // Nếu không tìm thấy hoặc mã không hợp lệ
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", "QR Code không hợp lệ hoặc đã được sử dụng."));
        } catch (IllegalStateException e) {
            // Lỗi nghiệp vụ (QR code đã sử dụng, người dùng quét lại, ...)
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Collections.singletonMap("message", e.getMessage()));
        } catch (IOException e) {
            // Lỗi upload ảnh
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Không thể tải ảnh chứng minh lên máy chủ."));
        } catch (Exception e) {
            // Lỗi chung
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Đã xảy ra lỗi khi xử lý QR Code."));
        }
    }


    // API làm mới QR Code
    @PostMapping("/qrcode/refresh")
    public ResponseEntity<?> refreshQRCode(@RequestParam Long sponsorId, @RequestParam Long qrCodeId) {
        try {
            SponsorQRCode refreshedQrCode = sponsorQRCodeService.refreshQRCode(sponsorId, qrCodeId);

            // Nếu làm mới thành công, trả về QR code mới
            if (refreshedQrCode != null) {
                return ResponseEntity.ok(refreshedQrCode);
            }

            // Nếu không thể làm mới (QR code vẫn hợp lệ hoặc lỗi khác)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", "QR Code không cần làm mới hoặc không hợp lệ."));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Không thể làm mới QR Code."));
        }
    }
}
