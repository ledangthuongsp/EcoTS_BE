package com.example.EcoTS.Services.Sponsor;

import com.example.EcoTS.Models.SponsorQRCode;
import com.example.EcoTS.Repositories.SponsorQRCodeRepository;
import com.example.EcoTS.Services.CloudinaryService.CloudinaryService;
import com.example.EcoTS.Utils.MultipartFileWrapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Optional<SponsorQRCode> getActiveQRCode(Long sponsorId) {
        return sponsorQRCodeRepository.findBySponsorIdAndIsUsedFalse(sponsorId);
    }

    public SponsorQRCode useQRCode(Long qrCodeId, String userEmail, String proofImageUrl) {
        Optional<SponsorQRCode> optionalQRCode = sponsorQRCodeRepository.findByIdAndIsUsedFalse(qrCodeId);
        if (optionalQRCode.isPresent()) {
            SponsorQRCode sponsorQRCode = optionalQRCode.get();
            sponsorQRCode.setIsUsed(true);
            sponsorQRCode.setUserEmail(userEmail);
            sponsorQRCode.setProofImageUrl(proofImageUrl);
            return sponsorQRCodeRepository.save(sponsorQRCode);
        }
        return null;
    }

    public SponsorQRCode refreshQRCode(Long sponsorId, Long qrCodeId) throws IOException {
        Optional<SponsorQRCode> optionalQRCode = sponsorQRCodeRepository.findByIdAndIsUsedFalse(qrCodeId);
        if (optionalQRCode.isPresent()) {
            SponsorQRCode sponsorQRCode = optionalQRCode.get();
            if (sponsorQRCode.getExpiredAt().before(new Timestamp(System.currentTimeMillis()))) {
                String newContent = "Sponsor QR Code - Sponsor ID: " + sponsorId + " - Newsfeed ID: " + sponsorQRCode.getNewsfeedId();
                return generateQRCode(sponsorId, sponsorQRCode.getPoints(), sponsorQRCode.getNewsfeedId());
            }
        }
        return null;
    }

    public SponsorQRCode generateQRCode(Long sponsorId, Double points, Long newsfeedId) throws IOException {
        String content = "Sponsor QR Code - Sponsor ID: " + sponsorId + " - Newsfeed ID: " + newsfeedId;
        String qrCodeUrl = createAndUploadQRCode(content);

        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        Timestamp expiredAt = new Timestamp(currentTimestamp.getTime() + 3 * 60 * 1000);

        SponsorQRCode sponsorQRCode = new SponsorQRCode();
        sponsorQRCode.setSponsorId(sponsorId);
        sponsorQRCode.setNewsfeedId(newsfeedId);
        sponsorQRCode.setQrCodeUrl(qrCodeUrl);
        sponsorQRCode.setPoints(points);
        sponsorQRCode.setCreatedAt(currentTimestamp);
        sponsorQRCode.setExpiredAt(expiredAt);
        sponsorQRCode.setIsUsed(false);

        return sponsorQRCodeRepository.save(sponsorQRCode);
    }
}