package com.example.EcoTS.Controllers.Review;

import com.example.EcoTS.Models.Reviews;
import com.example.EcoTS.Models.Users;
import com.example.EcoTS.Repositories.ReviewRepository;
import com.example.EcoTS.Repositories.UserRepository;
import com.example.EcoTS.Services.CloudinaryService.CloudinaryService;
import com.example.EcoTS.Services.Review.ReviewService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/review")
@Tag(name ="Write Review", description = "User write review and rating the place they go to")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private ReviewRepository reviewRepository;
    @PostMapping(value = "/add", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Reviews> addReview(
            @RequestParam String username,
            @RequestParam Long locationId,
            @RequestParam String comment,
            @RequestParam int rating,
            @RequestPart(required = false) List<MultipartFile> attachments) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Reviews review = reviewService.addReview(user, locationId, comment, rating, attachments);
        return ResponseEntity.ok(review);
    }

    @GetMapping("/location/{locationId}")
    public ResponseEntity<List<Reviews>> getReviewsByLocation(@PathVariable Long locationId) {
        List<Reviews> reviews = reviewService.getReviewsByLocation(locationId);
        return ResponseEntity.ok(reviews);
    }
}
