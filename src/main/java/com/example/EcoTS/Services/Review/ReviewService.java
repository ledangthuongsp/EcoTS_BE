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

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private CloudinaryService cloudinaryService;

    public Reviews addReview(Users user, Long locationId, String comment, int rating, List<String> attachmentUrls) {
        Locations location = locationRepository.findById(locationId).orElseThrow(() -> new RuntimeException("Location not found"));

        Reviews review = Reviews.builder()
                .user(user)
                .location(location)
                .comment(comment)
                .rating(rating)
                .attachmentUrls(attachmentUrls)
                .build();
        return reviewRepository.save(review);
    }

    public List<Reviews> getReviewsByLocation(Long locationId) {
        Locations location = locationRepository.findById(locationId).orElseThrow(() -> new RuntimeException("Location not found"));
        return reviewRepository.findByLocation(location);
    }
}
