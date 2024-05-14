package com.example.EcoTS.Services.Location;

import com.example.EcoTS.DTOs.Request.Location.LocationDTO;
import com.example.EcoTS.Models.Locations;
import com.example.EcoTS.Repositories.LocationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import io.swagger.annotations.Api;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;
    public Locations createNewLocation(@RequestParam LocationDTO locationDTO)
    {
        Locations newLocation = new Locations();

        newLocation.setLocationName(locationDTO.getLocationName());
        newLocation.setDescription(locationDTO.getDescription());
        newLocation.setTypeOfLocation(locationDTO.getTypeOfLocation());
        newLocation.setLatitude(locationDTO.getLatitude());
        newLocation.setLongitude(locationDTO.getLongitude());
        return locationRepository.save(newLocation);
    }
    public List<Locations> getLocationsByType(String type) {
        return locationRepository.findByTypeOfLocation(type);
    }
    public List<Locations> getAllLocations(){
        return locationRepository.findAll();
    }
}
