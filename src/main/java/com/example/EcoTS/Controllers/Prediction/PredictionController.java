package com.example.EcoTS.Controllers.Prediction;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.cloudinary.json.JSONArray;
import org.cloudinary.json.JSONException;
import org.cloudinary.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
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
import java.io.InputStream;

@RestController
@RequestMapping("/detect")
@CrossOrigin
@Tag(name = "Detection Garbage")
@Tag(name = "Detection Garbage", description = "API for garbage detection using a machine learning model")
public class PredictionController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final Logger logger = LoggerFactory.getLogger(PredictionController.class);

    @Operation(summary = "Predict garbage type from image", description = "Uploads an image and returns the prediction of garbage type")
    @PostMapping(value = "/predict", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public String predict(@RequestPart("file") MultipartFile file) {
        try {
            String flaskApiUrl = "http://localhost:5000/detect";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // Create Multipart request
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename()));

            HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);

            logger.info("Sending request to Flask API");
            ResponseEntity<String> response = restTemplate.postForEntity(flaskApiUrl, entity, String.class);
            logger.info("Received response from Flask API: {}", response.getBody());

            // Extract class name from response
            String responseBody = response.getBody();
            JSONObject jsonResponse = new JSONObject(responseBody);
            JSONArray predictions = jsonResponse.getJSONArray("predictions");

            if (predictions.length() > 0) {
                JSONObject firstPrediction = predictions.getJSONObject(0);
                String className = firstPrediction.getString("class");
                return "Detected class: " + className;
            } else {
                return "No predictions found.";
            }
        } catch (IOException | JSONException e) {
            logger.error("Error during the request to Flask API", e);
            return "Error processing file.";
        }
    }

    private static class MultipartInputStreamFileResource extends InputStreamResource {
        private final String filename;

        MultipartInputStreamFileResource(InputStream inputStream, String filename) {
            super(inputStream);
            this.filename = filename;
        }

        @Override
        public String getFilename() {
            return this.filename;
        }

        @Override
        public long contentLength() throws IOException {
            return -1; // We do not know the content length in advance
        }
    }
}

