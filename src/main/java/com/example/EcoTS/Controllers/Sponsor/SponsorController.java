package com.example.EcoTS.Controllers.Sponsor;

import com.example.EcoTS.Models.Newsfeed.Newsfeed;
import com.example.EcoTS.Models.Sponsor;
import com.example.EcoTS.Models.SponsorQRCode;
import com.example.EcoTS.Repositories.Newsfeed.NewsfeedRepository;
import com.example.EcoTS.Repositories.SponsorRepository;
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
import java.util.*;

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
    @Autowired
    private SponsorRepository sponsorRepository;
    @Autowired
    private NewsfeedRepository newsfeedRepository;

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
                return ResponseEntity.ok(qrCode.get());
            } else {
                return ResponseEntity.ok(Collections.singletonMap("message", "Không tìm thấy QR code còn hiệu lực cho sponsorId: " + sponsorId));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Không thể lấy QR code còn hiệu lực cho sponsorId: " + sponsorId + ". Nguyên nhân: " + e.getMessage()));
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
    @PostMapping(value = "/qrcode/use")
    public ResponseEntity<?> useQRCode(@RequestParam Long qrCodeId, @RequestParam String userEmail) {
        try {


            // Gọi service để xử lý sử dụng QR code
            SponsorQRCode qrCode = sponsorQRCodeService.useQRCode(qrCodeId, userEmail);

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
        }  catch (Exception e) {
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
    // Login Sponsor
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        Sponsor sponsor = sponsorRepository.findByCompanyUsername(username);
        if (sponsor != null && sponsor.getCompanyPassword().equals(password)) {
            return ResponseEntity.ok("Login successful");
        }
        return ResponseEntity.status(401).body("Invalid username or password");
    }

    // Get Sponsor by Username
    @GetMapping("/get-by-username")
    public ResponseEntity<Sponsor> getSponsorByUsername(@RequestParam String username) {
        Sponsor sponsor = sponsorRepository.findByCompanyUsername(username);
        if (sponsor != null) {
            return ResponseEntity.ok(sponsor);
        }
        return ResponseEntity.status(404).body(null);
    }
    @GetMapping("/get-newsfeed-by-sponsor-id")
    public ResponseEntity<Double> getSponsorPoints (@RequestParam Long sponsorId) {

        double points = sponsorRepository.findCompanyPointsBySponsorId(sponsorId);
        return ResponseEntity.ok().body(points);
    }
    @GetMapping("/get-newsfeed-by-sponsor-id-with-status")
    public ResponseEntity<?> getNewsfeedWithStatus(@RequestParam Long sponsorId) {
        // Lấy tất cả các newsfeed của sponsor
        List<Newsfeed> newsfeeds = newsfeedRepository.findBySponsorId(sponsorId);

        // Lấy thời gian hiện tại
        Timestamp now = new Timestamp(System.currentTimeMillis());

        // Phân loại newsfeed theo trạng thái
        List<Newsfeed> upcoming = new ArrayList<>();
        List<Newsfeed> started = new ArrayList<>();
        List<Newsfeed> ended = new ArrayList<>();

        for (Newsfeed newsfeed : newsfeeds) {
            if (now.before(newsfeed.getStartedAt())) {
                // Upcoming: Chưa bắt đầu
                upcoming.add(newsfeed);
            } else if (now.after(newsfeed.getEndedAt())) {
                // Ended: Đã kết thúc
                ended.add(newsfeed);
            } else {
                // Started: Đang diễn ra
                started.add(newsfeed);
            }
        }

        // Trả về dữ liệu phân loại
        return ResponseEntity.ok(Map.of(
                "upcoming", upcoming,
                "started", started,
                "ended", ended
        ));
    }

}
