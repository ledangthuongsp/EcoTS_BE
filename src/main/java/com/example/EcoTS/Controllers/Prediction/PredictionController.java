package com.example.EcoTS.Controllers.Prediction;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
@RestController
@RequestMapping("/detect")
@CrossOrigin
@Tag(name = "Detection Garbage")
public class PredictionController {

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping(value = "/predict", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public String predict(@RequestPart("file") MultipartFile file) {
        try {
            String flaskApiUrl = "http://localhost:5000/detect";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };

            HttpEntity<Resource> entity = new HttpEntity<>(resource, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(flaskApiUrl, entity, String.class);
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return "Lỗi xử lý file.";
        }
    }
}
