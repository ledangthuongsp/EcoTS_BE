package com.example.EcoTS.Controllers.Prediction;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.codec.binary.Base64;

import java.io.IOException;

@RestController
@RequestMapping("/detect")
@CrossOrigin
@Tag(name = "Detection Garbage")
@Tag(name = "Detection Garbage", description = "API for garbage detection using a machine learning model")
public class PredictionController {

    private final RestTemplate restTemplate = new RestTemplate();

    @Operation(summary = "Predict garbage type from image", description = "Uploads an image and returns the prediction of garbage type")
    @PostMapping(value = "/predict", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public String predict(@RequestPart("file") MultipartFile file) {
        try {
            String flaskApiUrl = "http://localhost:5000/detect";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Convert file to Base64 string
            byte[] fileBytes = file.getBytes();
            String base64File = Base64.encodeBase64String(fileBytes);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("image", base64File);

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(flaskApiUrl, entity, String.class);
            return response.getBody();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error processing file.";
        }
    }
}
