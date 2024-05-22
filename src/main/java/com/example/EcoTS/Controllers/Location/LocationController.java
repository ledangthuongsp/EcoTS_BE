package com.example.EcoTS.Controllers.Location;

import com.example.EcoTS.DTOs.Request.Location.LocationDTO;
import com.example.EcoTS.Models.Locations;
import com.example.EcoTS.Services.Location.LocationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin
@RequestMapping("/location")
@Tag(name = "Location APIs", description = "Apis for get location and show it to front-end")
public class LocationController {

    @Autowired
    private LocationService locationService;
    @GetMapping("/get-by-type")
    public List<Locations> getLocationsByType(@RequestParam("type") String type) {
        return locationService.getLocationsByType(type);
    }
    @GetMapping("/get-all")
    public List<Locations> getAllLocations() {
        return locationService.getAllLocations();
    }

    @PutMapping("/create-new-location")
    public ResponseEntity<Locations> createNewLocation(@RequestBody LocationDTO locationDTO) {
        try {
            Locations newLocation = locationService.createNewLocation(locationDTO);
            return ResponseEntity.ok(newLocation);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(null);
        }
    }
}
