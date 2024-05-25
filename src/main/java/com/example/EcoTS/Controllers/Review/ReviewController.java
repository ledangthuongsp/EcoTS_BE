package com.example.EcoTS.Controllers.Review;

import com.example.EcoTS.Models.Reviews;
import com.example.EcoTS.Models.Users;
import com.example.EcoTS.Services.CloudinaryService.CloudinaryService;
import com.example.EcoTS.Services.Review.ReviewService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/review")
@Tag(name ="Write Review", description = "User write review and rating the place they go to")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @PostMapping("/add")
    public ResponseEntity<Reviews> addReview(
            @RequestParam Long locationId,
            @RequestParam String comment,
            @RequestParam int rating,
            @RequestParam(required = false) MultipartFile attachment) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users user = (Users) authentication.getPrincipal();

        String attachmentUrl = null;
        if (attachment != null) {
            attachmentUrl = cloudinaryService.uploadFileDonations(attachment);
        }

        Reviews review = reviewService.addReview(user, locationId, comment, rating, attachmentUrl);
        return ResponseEntity.ok(review);
    }
    @GetMapping("/location/{locationId}")
    public ResponseEntity<List<Reviews>> getReviewsByLocation(@PathVariable Long locationId) {
        List<Reviews> reviews = reviewService.getReviewsByLocation(locationId);
        return ResponseEntity.ok(reviews);
    }
}
