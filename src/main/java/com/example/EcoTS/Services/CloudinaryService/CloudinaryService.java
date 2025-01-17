package com.example.EcoTS.Services.CloudinaryService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class CloudinaryService {
    private static final Logger log = LoggerFactory.getLogger(CloudinaryService.class);
    @Value("${cloudinary.cloud-name}")
    private String cloudName;
    private final Cloudinary cloudinary;
    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }
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
    public String uploadFileNewsfeed(MultipartFile file) throws IOException {
        Map response = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "resource_type", "auto",
                        "folder", "Newsfeed" // Thêm folder "Newsfeed"
                ));
        return (String) response.get("url");
    }
    public List<String> uploadMultipleFilesNewsfeed(List<MultipartFile> files) throws IOException{
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            urls.add(uploadFileNewsfeed(file));
        }
        return urls;
    }
    public String userUploadAvatar(MultipartFile newAvatar, String oldAvatar) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(newAvatar.getBytes(),
                ObjectUtils.asMap(
                        "resource_type", "auto",
                        "folder", "UserAvatar"
                ));
        String newAvatarUrl = (String) uploadResult.get("url");

        if (oldAvatar != null && !oldAvatar.isEmpty()) {
            try {
                deleteAvatar(extractPublicIdFromUrl(oldAvatar));
            } catch (IllegalArgumentException | URISyntaxException e) {
                log.error("Failed to delete old avatar: " + e.getMessage());
            }
        }
        return newAvatarUrl;
    }

    private void deleteAvatar(String publicId) throws IOException {
        Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.asMap(
                "resource_type", "image",
                "invalidate", true
        ));
        log.info("Deleted image with public_id: " + publicId);
    }

    private String extractPublicIdFromUrl(String avatarUrl) throws URISyntaxException, IllegalArgumentException {
        URI uri = new URI(avatarUrl);
        String path = uri.getPath();
        int uploadIndex = path.lastIndexOf("upload/");
        if (uploadIndex == -1) {
            throw new IllegalArgumentException("URL does not contain 'upload/'");
        }
        int startIndex = uploadIndex + "upload/".length();
        int endIndex = path.lastIndexOf('.');
        if (endIndex == -1) {
            throw new IllegalArgumentException("URL does not have a proper file extension");
        }
        return path.substring(startIndex, endIndex);
    }
    public String uploadFileReview(MultipartFile file) throws IOException {
        Map response = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "resource_type", "auto",
                        "folder", "Reviews" // Thêm folder "Reviews"
                ));
        return (String) response.get("url");
    }

    public List<String> uploadMultipleFilesReview(List<MultipartFile> files) throws IOException {
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            urls.add(uploadFileReview(file));
        }
        return urls;
    }
    public String uploadFileLocation(MultipartFile file) throws IOException {
        Map response = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "resource_type", "auto",
                        "folder", "Locations" // Thêm folder "Reviews"
                ));
        return (String) response.get("url");
    }

    public List<String> uploadMultipleFilesLocations(List<MultipartFile> files) throws IOException {
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            urls.add(uploadFileLocation(file));
        }
        return urls;
    }
    public String uploadFileAchievement(MultipartFile file) throws IOException {
        Map response = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "resource_type", "auto",
                        "folder", "Achievement" // Thêm folder "Reviews"
                ));
        return (String) response.get("url");
    }
    public String uploadFileLogoAchievement( MultipartFile file) throws IOException {
        Map response = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "resource_type", "auto",
                        "folder", "Logo Achievement" // Thêm folder "Reviews"
                ));
        return (String) response.get("url");
    }
    public String uploadFileQuizTopic( MultipartFile file) throws IOException {
        Map response = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "resource_type", "auto",
                        "folder", "Quiz" // Thêm folder "Reviews"
                ));
        return (String) response.get("url");
    }

    public String uploadDetectReponse( MultipartFile file) throws IOException {
        Map response = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "resource_type", "auto",
                        "folder", "Detect Response" // Thêm folder "Reviews"
                ));
        return (String) response.get("url");
    }
    public String uploadCommentImage(MultipartFile file) throws IOException
    {
        Map response = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "resource_type", "auto",
                        "folder", "Comment" // Thêm folder "Reviews"
                ));
        return (String) response.get("url");
    }
    public List<String> uploadMultipleCommetImage(List<MultipartFile> files) throws IOException
    {
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            urls.add(uploadCommentImage(file));
        }
        return urls;
    }
}


