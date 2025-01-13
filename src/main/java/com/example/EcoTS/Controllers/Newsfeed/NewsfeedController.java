package com.example.EcoTS.Controllers.Newsfeed;

import com.example.EcoTS.DTOs.Request.Newsfeed.NewsfeedRequest;
import com.example.EcoTS.DTOs.Response.Newsfeed.NewsfeedResponse;
import com.example.EcoTS.Models.Newsfeed.Newsfeed;
import com.example.EcoTS.Services.Newsfeed.NewsfeedService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/newsfeed")
@Tag(name = "Newsfeed CRUD")
public class NewsfeedController {
    @Autowired
    private NewsfeedService newsfeedService;

    @PostMapping
    public ResponseEntity<NewsfeedResponse> createOrUpdateNewsfeed(@RequestBody NewsfeedRequest requestDTO) {
        NewsfeedResponse response = newsfeedService.createOrUpdateNewsfeed(requestDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsfeedResponse> getNewsfeed(@PathVariable Long id) {
        NewsfeedResponse response = newsfeedService.getNewsfeed(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<NewsfeedResponse>> getAllNewsfeeds() {
        List<NewsfeedResponse> response = newsfeedService.getAllNewsfeeds();
        return ResponseEntity.ok(response);
    }
}
