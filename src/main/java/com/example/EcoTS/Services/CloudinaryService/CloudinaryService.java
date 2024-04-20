package com.example.EcoTS.Services.CloudinaryService;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CloudinaryService {
    @Value("${cloudinary.cloud-name}")
    private String cloudName;
    @Autowired
    private Cloudinary cloudinary;

    public String uploadFileDonations(MultipartFile file) throws IOException {
        Map response = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "resource_type", "auto",
                        "folder", "Donations" // Thêm folder "Donations"
                ));
        return (String) response.get("url");
    }
    public List<String> uploadMultipleFilesDonations(List<MultipartFile> files) throws IOException {
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            urls.add(uploadFileDonations(file));
        }
        return urls;
    }

    public String userUploadAvatar(MultipartFile newAvatar, String oldAvatar) throws IOException {
        // Tải lên avatar mới
        Map uploadResult = cloudinary.uploader().upload(newAvatar.getBytes(),
                ObjectUtils.asMap(
                        "resource_type", "auto",
                        "folder", "User Avatar"
                ));
        String newAvatarUrl = (String) uploadResult.get("url");

        // Kiểm tra và xóa avatar cũ nếu nó tồn tại
        if (oldAvatar != null && !oldAvatar.isEmpty() && avatarExists(oldAvatar)) {
            deleteAvatar(oldAvatar);
        }

        return newAvatarUrl;
    }
    // Phương thức kiểm tra xem avatar có tồn tại trên Cloudinary không
    private boolean avatarExists(String avatarUrl) {
        try {
            Map result = cloudinary.uploader().upload(avatarUrl, ObjectUtils.asMap(
                    "resource_type", "image",
                    "folder", "User Avatar",
                    "public_id", extractPublicIdFromUrl(avatarUrl)
            ));
            return result != null && result.get("existing") == Boolean.TRUE;
        } catch (IOException e) {
            return false;
        }
    }
    private void deleteAvatar(String avatarUrl) throws IOException {
        if (avatarUrl != null && !avatarUrl.isEmpty()) {
            // Trích xuất public_id từ URL
            String publicId = extractPublicIdFromUrl(avatarUrl);
            if (publicId != null && !publicId.isEmpty()) {
                cloudinary.uploader().destroy(publicId, ObjectUtils.asMap(
                        "resource_type", "image"
                ));
            }
        }
    }
    private String extractPublicIdFromUrl(String avatarUrl) {
        try {
            // Giả định rằng URL của bạn có dạng:
            // http://res.cloudinary.com/{cloud_name}/image/upload/v{version}/{public_id}.{file_extension}
            // Bạn cần cắt chuỗi để lấy phần {public_id}
            URI uri = new URI(avatarUrl);
            String path = uri.getPath();
            String publicId = path.split("/")[path.split("/").length - 1]; // Lấy phần tử cuối cùng của path
            publicId = publicId.substring(0, publicId.lastIndexOf('.')); // Loại bỏ phần mở rộng file
            return publicId;
        } catch (URISyntaxException e) {
            // Xử lý lỗi nếu URL không hợp lệ
            return null;
        }
    }
}


