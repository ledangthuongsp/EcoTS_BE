package com.example.EcoTS.Services.CloudinaryService;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CloudinaryService {
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
    public String userUploadAvatar(MultipartFile file) throws IOException {
        Map response = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "resource_type", "auto",
                        "folder", "User Avatar"
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
}


