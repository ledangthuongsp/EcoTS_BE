package com.example.EcoTS.Services.Location;

import com.example.EcoTS.DTOs.Request.Location.LocationDTO;
import com.example.EcoTS.Models.Locations;
import com.example.EcoTS.Repositories.LocationRepository;

import com.example.EcoTS.Services.CloudinaryService.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private CloudinaryService cloudinaryService;

    @Transactional
    public Locations createNewLocation(String name, String des, String address, double i, double l, MultipartFile backGroundImage, List<MultipartFile> imageDetails) throws IOException {
        Locations newLocation = new Locations();
        String backGroundImgUrl = cloudinaryService.uploadFileLocation(backGroundImage);
        List<String> imgDetailsUrl = cloudinaryService.uploadMultipleFilesLocations(imageDetails);
        newLocation.setLocationName(name);
        newLocation.setDescription(des);
        newLocation.setTypeOfLocation(address);
        newLocation.setLatitude(i);
        newLocation.setLongitude(l);
        newLocation.setBackGroundImgUrl(backGroundImgUrl);
        newLocation.setImgDetailsUrl(imgDetailsUrl);
        return locationRepository.save(newLocation);
    }
    @Transactional
    public List<Locations> getLocationsByType(String type) {
        return locationRepository.findByTypeOfLocation(type);
    }

    @Transactional
    public List<Locations> getAllLocations(){
        return locationRepository.findAll();
    }

    @Transactional
    public Locations updateInfoLocation(Long locationId, String description, String address, double latitude, double longitude, MultipartFile backGroundImage, List<MultipartFile> imageDetails) throws IOException {
        Locations updateLocation = locationRepository.findById(locationId).orElseThrow(() -> new IllegalArgumentException("Location not found"));

        updateLocation.setDescription(description);
        updateLocation.setTypeOfLocation(address);
        updateLocation.setLatitude(latitude);
        updateLocation.setLongitude(longitude);

        if (backGroundImage != null && !backGroundImage.isEmpty()) {
            String backGroundImgUrl = cloudinaryService.uploadFileLocation(backGroundImage);
            updateLocation.setBackGroundImgUrl(backGroundImgUrl);
        }

        if (imageDetails != null && !imageDetails.isEmpty()) {
            List<String> imgDetailsUrl = cloudinaryService.uploadMultipleFilesLocations(imageDetails);
            updateLocation.setImgDetailsUrl(imgDetailsUrl);
        }

        return locationRepository.save(updateLocation);
    }

    @Transactional(readOnly = true)
    public Locations addEmployeeToLocation(Long employeeId, Long locationId) {
        Locations locations = locationRepository.findById(locationId).orElseThrow();
        List<Long> employeeList;
        if (locations.getEmployeeId() == null) {
            employeeList = new ArrayList<>();
        } else {
            employeeList = locations.getEmployeeId();
        }
        employeeList.add(employeeId);
        locations.setEmployeeId(employeeList);
        return locationRepository.save(locations);
    }
}
