package com.example.EcoTS.Controllers.Location;

import com.example.EcoTS.DTOs.Request.Location.*;
import com.example.EcoTS.DTOs.Response.Location.LocationResponseDTO;
import com.example.EcoTS.DTOs.Response.Location.LocationWithDistanceDTO;
import com.example.EcoTS.Models.Locations;
import com.example.EcoTS.Services.Location.LocationService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
@RequestMapping("/location")
@Tag(name = "Location APIs", description = "Apis for get location and show it to front-end")
@RequiredArgsConstructor

public class LocationController {

    private final LocationService locationService;

    @PostMapping(value = "/sponsor/{sponsorId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<LocationResponseDTO> createLocation(
            @PathVariable Long sponsorId,
            @RequestParam String locationName,
            @RequestParam String description,
            @RequestParam String address,
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestPart MultipartFile backGroundImage,
            @RequestPart(required = false) List<MultipartFile> imageDetails
    ) throws IOException {
        return ResponseEntity.ok(locationService.createLocation(
                sponsorId, locationName, description, address,
                latitude, longitude, backGroundImage, imageDetails
        ));
    }

    @PutMapping(value = "/{locationId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<LocationResponseDTO> updateLocation(
            @PathVariable Long locationId,
            @RequestParam String locationName,
            @RequestParam String description,
            @RequestParam String address,
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestPart(required = false) MultipartFile backGroundImage,
            @RequestPart(required = false) List<MultipartFile> imageDetails
    ) throws IOException {
        return ResponseEntity.ok(locationService.updateLocation(
                locationId, locationName, description, address,
                latitude, longitude, backGroundImage, imageDetails
        ));
    }

    @DeleteMapping("/{locationId}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long locationId) {
        locationService.deleteLocation(locationId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<LocationResponseDTO>> getAllLocations() {
        return ResponseEntity.ok(locationService.getAllLocations());
    }

    @GetMapping("/{locationId}")
    public ResponseEntity<LocationResponseDTO> getLocationById(@PathVariable Long locationId) {
        return ResponseEntity.ok(locationService.getLocationById(locationId));
    }

    @GetMapping("/search/by-material")
    public ResponseEntity<List<LocationResponseDTO>> getByMaterial(@RequestParam Long materialId) {
        return ResponseEntity.ok(locationService.findLocationsByMaterial(materialId));
    }

    @GetMapping("/search/by-date")
    public ResponseEntity<List<LocationResponseDTO>> getByOpenDay(@RequestParam String day) {
        return ResponseEntity.ok(locationService.findLocationsByDayOfWeek(day));
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<LocationWithDistanceDTO>> getNearbyLocations(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam(defaultValue = "5") double radiusKm
    ) {
        return ResponseEntity.ok(locationService.findNearbyLocations(lat, lng, radiusKm));
    }

    @PutMapping("/update-materials")
    public ResponseEntity<String> updateMaterialsForLocation(@RequestBody AssignMaterialsRequest request) {
        locationService.updateMaterialsForLocation(request.getLocationId(), request.getMaterialIds());
        return ResponseEntity.ok("Materials updated successfully.");
    }

    @DeleteMapping("/{locationId}/remove-materials")
    public ResponseEntity<String> removeAllMaterials(@PathVariable Long locationId) {
        locationService.removeAllMaterials(locationId);
        return ResponseEntity.ok("All materials removed.");
    }

    // SCHEDULES
    @PostMapping("/schedules")
    public ResponseEntity<LocationResponseDTO> addOpeningSchedule(@RequestBody AddScheduleRequest request) {
        return ResponseEntity.ok(locationService.addOpeningSchedule(request));
    }

    @PutMapping("/schedules/dayOfWeek")
    public ResponseEntity<LocationResponseDTO> updateOpeningScheduleDayOfWeek(@RequestBody UpdateExistingScheduleRequest request) {
        return ResponseEntity.ok(locationService.updateOpeningScheduleDayOfWeek(request));
    }

    @PostMapping("/schedules/time-slots")
    public ResponseEntity<LocationResponseDTO> addTimeSlotsToSchedule(@RequestBody ManageTimeSlotRequest request) {
        return ResponseEntity.ok(locationService.addTimeSlotsToSchedule(request));
    }

    @PutMapping("/schedules/time-slots")
    public ResponseEntity<LocationResponseDTO> updateTimeSlotsInSchedule(@RequestBody ManageTimeSlotRequest request) {
        return ResponseEntity.ok(locationService.updateTimeSlotsInSchedule(request));
    }

    @DeleteMapping("/schedules/{scheduleId}/time-slots")
    public ResponseEntity<LocationResponseDTO> removeTimeSlotsFromSchedule(
            @PathVariable Long scheduleId,
            @RequestBody List<Long> timeSlotIds) {
        return ResponseEntity.ok(locationService.removeTimeSlotsFromSchedule(scheduleId, timeSlotIds));
    }


    @DeleteMapping("/schedules/{locationId}/{dayOfWeek}") // Sử dụng PathVariable cho dayOfWeek
    public ResponseEntity<String> deleteScheduleByDay(
            @PathVariable Long locationId,
            @PathVariable String dayOfWeek
    ) {
        locationService.deleteSchedule(locationId, dayOfWeek);
        return ResponseEntity.ok("Schedule deleted successfully.");
    }

}

