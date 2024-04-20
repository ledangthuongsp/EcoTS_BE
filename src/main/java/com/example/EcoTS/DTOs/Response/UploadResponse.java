package com.example.EcoTS.DTOs.Response;

public class UploadResponse {

    private String imageUrl;

    public UploadResponse(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
