package com.example.EcoTS.Controllers.Newsfeed;

import com.example.EcoTS.DTOs.Request.Newsfeed.NewsfeedRequest;
import com.example.EcoTS.DTOs.Request.Newsfeed.PollRequest;
import com.example.EcoTS.DTOs.Response.Newsfeed.NewsfeedResponse;
import com.example.EcoTS.Models.Newsfeed.Newsfeed;
import com.example.EcoTS.Services.Newsfeed.NewsfeedService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/newsfeed")
@Tag(name = "Newsfeed CRUD")
public class NewsfeedController {
    @Autowired
    private NewsfeedService newsfeedService;

    // CREATE: Add a new newsfeed
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Newsfeed> createNewsfeed(
            @RequestParam String content,
            @RequestParam (required = false) Long sponsorId,
            @RequestParam (required = false) Double pointForActivity,
            @RequestParam (required = false) Long userId,
            @RequestParam List<String> pollOptions, // Các lựa chọn cho Poll
            @RequestPart("files") List<MultipartFile> files // Danh sách file tải lên
    ) throws IOException {
        Newsfeed createdNewsfeed = newsfeedService.createNewsfeed(content, sponsorId, pointForActivity, userId, pollOptions, files);
        return ResponseEntity.ok(createdNewsfeed);
    }




    // READ: Get all newsfeeds
    @GetMapping("/getAll")
    public ResponseEntity<List<Newsfeed>> getAllNewsfeeds() {
        List<Newsfeed> newsfeeds = newsfeedService.getAllNewsfeed();
        return ResponseEntity.ok(newsfeeds);
    }
    @GetMapping("/get-your-activity")
    public ResponseEntity<List<Newsfeed>> getNewsfeedYourActivity(Long userId)
    {
        List<Newsfeed> newsfeeds = newsfeedService.getYourActivity(userId);

        return ResponseEntity.ok(newsfeeds);
    }
    // READ: Get a single newsfeed by I

    // DELETE: Delete a newsfeed by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteNewsfeed(@PathVariable Long id) {
        newsfeedService.deleteNewsfeed(id);
        return ResponseEntity.ok("Newsfeed deleted successfully");
    }
}
