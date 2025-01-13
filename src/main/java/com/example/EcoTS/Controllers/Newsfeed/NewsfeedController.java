package com.example.EcoTS.Controllers.Newsfeed;

import com.example.EcoTS.DTOs.Request.Newsfeed.NewsfeedRequest;
import com.example.EcoTS.DTOs.Request.Newsfeed.PollRequest;
import com.example.EcoTS.DTOs.Response.Newsfeed.NewsfeedResponse;
import com.example.EcoTS.Models.Newsfeed.Newsfeed;
import com.example.EcoTS.Services.Newsfeed.NewsfeedService;
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
    @PostMapping(value = "/admin/newsfeed/create", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Newsfeed> createNewsfeed(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestPart("imageUrl") List<MultipartFile> mediaUrls,
            @RequestParam(value = "pollRequest", required = false) PollRequest pollRequest) throws IOException {

        Newsfeed createdNewsfeed = newsfeedService.createNewsfeed(title, content, mediaUrls, pollRequest);
        return ResponseEntity.ok(createdNewsfeed);
    }


    // READ: Get all newsfeeds
    @GetMapping("/newsfeed/all")
    public ResponseEntity<List<NewsfeedResponse>> getAllNewsfeeds() {
        List<NewsfeedResponse> newsfeeds = newsfeedService.getAllNewsfeeds();

        return ResponseEntity.ok(newsfeeds);
    }

    // READ: Get a single newsfeed by ID
    @GetMapping("/newsfeed/{id}")
    public ResponseEntity<Newsfeed> getNewsfeedById(@PathVariable Long id) {
        Newsfeed newsfeed = newsfeedService.getNewsfeedById(id);
        return ResponseEntity.ok(newsfeed);
    }

    // UPDATE: Update an existing newsfeed by ID
    @PutMapping(value = "/admin/newsfeed/update/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Newsfeed> updateNewsfeed(
            @PathVariable Long id,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "content", required = false) String content,
            @RequestPart(value = "imageUrl", required = false) List<MultipartFile> imageUrl) throws IOException {

        Newsfeed updatedNewsfeed = newsfeedService.updateNewsfeed(id, title, content, imageUrl);
        return ResponseEntity.ok(updatedNewsfeed);
    }

    // DELETE: Delete a newsfeed by ID
    @DeleteMapping("/admin/newsfeed/delete/{id}")
    public ResponseEntity<String> deleteNewsfeed(@PathVariable Long id) {
        newsfeedService.deleteNewsfeed(id);
        return ResponseEntity.ok("Newsfeed deleted successfully");
    }
}
