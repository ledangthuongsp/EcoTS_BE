package com.example.EcoTS.Controllers.DetectResponse;


import com.example.EcoTS.Models.DetectResponse;
import com.example.EcoTS.Repositories.DetectResponseRepository;
import com.example.EcoTS.Services.DetectResponse.DetectResponseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@Tag(name = "Detect Response To Server")
@RequestMapping("/detect-response")
public class DetectResponseController {
    @Autowired
    private DetectResponseService detectResponseService;

    @PostMapping(value = "/send-response", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> uploadDetectResponse(
            @RequestParam("userId") Long userId,
            @RequestParam("description") String description,
            @RequestPart("file") MultipartFile file) {

        try {
            if (description == null || description.isEmpty()) {
                return ResponseEntity.badRequest().body("Description cannot be empty");
            }

            if (userId == null || userId <= 0) {
                return ResponseEntity.badRequest().body("Invalid userId");
            }

            // Call service to handle file upload and database save
            detectResponseService.uploadFile(userId, description, file);
            return ResponseEntity.ok("Response uploaded successfully");

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @Autowired
    private DetectResponseRepository detectResponseRepository;

    @GetMapping("/all")
    public ResponseEntity<List<DetectResponse>> getAllDetectResponses() {
        List<DetectResponse> detectResponses = detectResponseRepository.findAll();
        return ResponseEntity.ok(detectResponses);
    }
}
