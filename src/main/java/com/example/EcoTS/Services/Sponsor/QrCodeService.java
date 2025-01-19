package com.example.EcoTS.Services.Sponsor;

import com.example.EcoTS.Services.CloudinaryService.CloudinaryService;
import com.example.EcoTS.Utils.MultipartFileWrapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;

@Service
public class QrCodeService {
    // Phương thức tạo QR code
    @Autowired
    private CloudinaryService cloudinaryService;

    // Phương thức tạo QR code
    public byte[] generateQRCode(String content) throws IOException {
        try {
            // Tạo QR code
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
            hints.put(EncodeHintType.MARGIN, 1);
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 300, 300, hints);

            // Chuyển BitMatrix thành BufferedImage
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            bufferedImage.createGraphics();
            Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, width, height);
            graphics.setColor(Color.BLACK);

            // Vẽ QR code lên hình ảnh
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    if (bitMatrix.get(i, j)) {
                        graphics.fillRect(i, j, 1, 1);
                    }
                }
            }

            // Chuyển BufferedImage thành byte array
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            javax.imageio.ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            throw new IOException("Error generating QR Code", e);
        }
    }

    // Phương thức tạo và upload QR code lên Cloudinary
    public String createAndUploadQRCode(String content) throws IOException {
        // Tạo QR code dưới dạng byte array
        byte[] qrCodeImage = generateQRCode(content);

        // Upload QR code lên Cloudinary và nhận lại URL
        return cloudinaryService.uploadSponsorQrCode(new MultipartFileWrapper(qrCodeImage));
    }
}
