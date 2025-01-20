package com.example.EcoTS.Services.Sponsor;

import com.example.EcoTS.Models.*;
import com.example.EcoTS.Repositories.*;
import com.example.EcoTS.Services.CloudinaryService.CloudinaryService;
import com.example.EcoTS.Utils.MultipartFileWrapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.Optional;

@Service
public class SponsorQRCodeService {

    @Autowired
    private SponsorQRCodeRepository sponsorQRCodeRepository;

    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SponsorRepository sponsorRepository;
    @Autowired
    private PointRepository pointRepository;
    @Autowired
    private QRCodeUsageRepository qrCodeUsageRepository;

    @Transactional
    public byte[] generateQRCode(String content) throws IOException {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
            hints.put(EncodeHintType.MARGIN, 1);
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 300, 300, hints);

            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            bufferedImage.createGraphics();
            Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, width, height);
            graphics.setColor(Color.BLACK);

            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    if (bitMatrix.get(i, j)) {
                        graphics.fillRect(i, j, 1, 1);
                    }
                }
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            javax.imageio.ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            throw new IOException("Error generating QR Code", e);
        }
    }

    public String createAndUploadQRCode(String content) throws IOException {
        byte[] qrCodeImage = generateQRCode(content);
        return cloudinaryService.uploadSponsorQrCode(new MultipartFileWrapper(qrCodeImage));
    }
    @Transactional
    public Optional<SponsorQRCode> getActiveQRCode(Long sponsorId) {
        java.util.List<SponsorQRCode> qrCodes = sponsorQRCodeRepository.findBySponsorIdAndIsUsedFalse(sponsorId);

        // Lọc danh sách để tìm QR Code còn hạn
        return qrCodes.stream()
                .filter(qrCode -> qrCode.getExpiredAt() != null
                        && qrCode.getExpiredAt().after(new Timestamp(System.currentTimeMillis())))
                .findFirst();
    }


    @Transactional
    public SponsorQRCode useQRCode(Long qrCodeId, String userEmail, String proofImageUrl) {
        SponsorQRCode qrCode = sponsorQRCodeRepository.findById(qrCodeId)
                .orElseThrow(() -> new ResourceNotFoundException("QR Code not found"));

        // Kiểm tra xem QR code đã hết hạn chưa
        if (qrCode.getExpiredAt().before(new Timestamp(System.currentTimeMillis()))) {
            throw new IllegalStateException("QR Code đã hết hạn.");
        }

        // Kiểm tra trạng thái QR Code
        if (qrCode.getIsUsed()) {
            throw new IllegalStateException("QR Code đã được sử dụng.");
        }

        // Kiểm tra nếu user đã sử dụng mã này
        boolean alreadyUsed = qrCodeUsageRepository.existsBySponsorQRCodeAndUserEmail(qrCode, userEmail);
        if (alreadyUsed) {
            throw new IllegalStateException("Bạn đã sử dụng mã QR này.");
        }

        // Lưu thông tin sử dụng
        QRCodeUsage usage = QRCodeUsage.builder()
                .sponsorQRCode(qrCode)
                .userEmail(userEmail)
                .proofImageUrl(proofImageUrl)
                .usedAt(new Timestamp(System.currentTimeMillis()))
                .build();
        qrCodeUsageRepository.save(usage);

        // Cập nhật điểm
        Sponsor sponsor = sponsorRepository.findById(qrCode.getSponsorId())
                .orElseThrow(() -> new ResourceNotFoundException("Sponsor not found"));
        Users user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Points points = pointRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Points not found"));

        // Trừ điểm từ Sponsor
        double sponsorPoints = sponsor.getCompanyPoints() - qrCode.getPoints();
        if (sponsorPoints < 0) {
            throw new IllegalArgumentException("Sponsor không đủ điểm.");
        }
        sponsor.setCompanyPoints(sponsorPoints);
        sponsorRepository.save(sponsor);

        // Cộng điểm cho User
        double userPoints = points.getPoint() + qrCode.getPoints();
        points.setPoint(userPoints);
        pointRepository.save(points);

        return qrCode;
    }


    public SponsorQRCode refreshQRCode(Long sponsorId, Long qrCodeId) throws IOException {
        Optional<SponsorQRCode> optionalQRCode = sponsorQRCodeRepository.findByIdAndIsUsedFalse(qrCodeId);
        if (optionalQRCode.isPresent()) {
            SponsorQRCode sponsorQRCode = optionalQRCode.get();
            if (sponsorQRCode.getExpiredAt().before(new Timestamp(System.currentTimeMillis()))) {
                String newContent = "Sponsor QR Code - Sponsor ID: " + sponsorId + " - Newsfeed ID: " + sponsorQRCode.getNewsfeedId() + " - Qr ID: " + qrCodeId;
                return generateQRCode(sponsorId, sponsorQRCode.getPoints(), sponsorQRCode.getNewsfeedId(), qrCodeId);
            }
        }
        return null;
    }

    public SponsorQRCode generateQRCode(Long sponsorId, Double points, Long newsfeedId, Long qrCodeId) throws IOException {
        String content = "Sponsor QR Code - Sponsor ID: " + sponsorId + " - Newsfeed ID: " + newsfeedId + " - Qr ID: " + qrCodeId;
        String qrCodeUrl = createAndUploadQRCode(content);

        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        Timestamp expiredAt = new Timestamp(currentTimestamp.getTime() + 3 * 60 * 1000);

        SponsorQRCode sponsorQRCode = SponsorQRCode.builder()
                .sponsorId(sponsorId)
                .qrCodeUrl(qrCodeUrl)
                .points(points)
                .newsfeedId(newsfeedId)
                .createdAt(currentTimestamp)
                .expiredAt(expiredAt)
                .isUsed(false)
                .build();

        return sponsorQRCodeRepository.save(sponsorQRCode);
    }

}