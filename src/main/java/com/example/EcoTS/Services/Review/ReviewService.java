package com.example.EcoTS.Services.Review;


import com.example.EcoTS.Models.Locations;
import com.example.EcoTS.Models.Reviews;
import com.example.EcoTS.Models.Users;
import com.example.EcoTS.Repositories.LocationRepository;
import com.example.EcoTS.Repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private LocationRepository locationRepository;

    public Reviews addReview(Users user, Long locationId, String comment, int rating, String attachmentUrl) {
        Locations location = locationRepository.findById(locationId).orElseThrow(() -> new RuntimeException("Location not found"));

        Reviews review = Reviews.builder()
                .user(user)
                .location(location)
                .comment(comment)
                .rating(rating)
                .attachmentUrl(attachmentUrl)
                .build();

        return reviewRepository.save(review);
    }
    public List<Reviews> getReviewsByLocation(Long locationId) {
        Locations location = locationRepository.findById(locationId).orElseThrow(() -> new RuntimeException("Location not found"));
        return reviewRepository.findByLocation(location);
    }
}
