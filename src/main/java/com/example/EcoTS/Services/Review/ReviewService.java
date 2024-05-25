package com.example.EcoTS.Services.Review;


import com.example.EcoTS.Models.Locations;
import com.example.EcoTS.Models.Reviews;
import com.example.EcoTS.Models.Users;
import com.example.EcoTS.Repositories.LocationRepository;
import com.example.EcoTS.Repositories.ReviewRepository;
import com.example.EcoTS.Services.CloudinaryService.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private CloudinaryService cloudinaryService;

    public Reviews addReview(Users user, Long locationId, String comment, int rating, List<MultipartFile> attachmentUrls) throws IOException {
        Locations location = locationRepository.findById(locationId).orElseThrow(() -> new RuntimeException("Location not found"));
        List<String> attachments = cloudinaryService.uploadMultipleFilesReview(attachmentUrls);
        Reviews review = new Reviews();
        review.setUser(user);
        review.setLocation(location);
        review.setComment(comment);
        review.setRating(rating);
        review.setAttachmentUrls(attachments);
        reviewRepository.save(review);
        return review;
    }

    public List<Reviews> getReviewsByLocation(Long locationId) {
        Locations location = locationRepository.findById(locationId).orElseThrow(() -> new RuntimeException("Location not found"));
        return reviewRepository.findByLocation(location);
    }
}
