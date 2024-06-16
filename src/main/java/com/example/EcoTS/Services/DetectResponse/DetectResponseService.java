package com.example.EcoTS.Services.DetectResponse;

import com.example.EcoTS.Models.DetectResponse;
import com.example.EcoTS.Repositories.DetectResponseRepository;
import com.example.EcoTS.Services.CloudinaryService.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class DetectResponseService {
    @Autowired
    private DetectResponseRepository detectResponseRepository;
//    @Autowired
//    private DetectResponse detectResponse;
    @Autowired
    private CloudinaryService cloudinaryService;

    public DetectResponse uploadFile(Long userId, String description, MultipartFile file) throws IOException {
        String imgUrl = cloudinaryService.uploadDetectReponse(file);
        DetectResponse detectResponse = new DetectResponse();
        detectResponse.setDescription(description);
        detectResponse.setImgUrl(imgUrl);
        detectResponse.setUserId(userId);
        return detectResponseRepository.save(detectResponse);
    }
}
