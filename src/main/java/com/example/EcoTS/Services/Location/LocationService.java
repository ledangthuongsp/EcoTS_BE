package com.example.EcoTS.Services.Location;

import com.example.EcoTS.DTOs.Request.Location.LocationDTO;
import com.example.EcoTS.DTOs.Response.Location.LocationResponseDTO;
import com.example.EcoTS.DTOs.Response.Location.TimeSlotDTO;
import com.example.EcoTS.Enum.DayOfWeek;
import com.example.EcoTS.Models.*;
import com.example.EcoTS.Repositories.LocationRepository;

import com.example.EcoTS.Repositories.MaterialRepository;
import com.example.EcoTS.Repositories.SponsorRepository;
import com.example.EcoTS.Services.CloudinaryService.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import com.example.EcoTS.DTOs.Request.Location.LocationDTO;
import com.example.EcoTS.DTOs.Response.Location.LocationResponseDTO;
import com.example.EcoTS.Models.Locations;
import com.example.EcoTS.Models.Sponsor;
import com.example.EcoTS.Repositories.LocationRepository;
import com.example.EcoTS.Repositories.MaterialRepository;
import com.example.EcoTS.Repositories.SponsorRepository;
import com.example.EcoTS.Services.CloudinaryService.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final SponsorRepository sponsorRepository;
    private final MaterialRepository materialRepository;
    private final LocationMapper mapper;
    private final CloudinaryService cloudinaryService;

    public LocationResponseDTO createLocation(Long sponsorId, String name, String desc, String addr,
                                              double lat, double lng,
                                              MultipartFile bg, List<MultipartFile> imgs) throws IOException {
        Sponsor sponsor = sponsorRepository.findById(sponsorId)
                .orElseThrow(() -> new RuntimeException("Sponsor not found"));

        String bgUrl = upload(bg);
        List<String> imgUrls = uploadList(imgs);

        Locations location = Locations.builder()
                .locationName(name)
                .description(desc)
                .typeOfLocation("default")
                .latitude(lat)
                .longitude(lng)
                .backGroundImgUrl(bgUrl)
                .imgDetailsUrl(imgUrls)
                .sponsor(sponsor)
                .build();

        return mapper.toDTO(locationRepository.save(location));
    }

    public LocationResponseDTO updateLocation(Long id, String name, String desc, String addr,
                                              double lat, double lng,
                                              MultipartFile bg, List<MultipartFile> imgs) throws IOException {
        Locations location = locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found"));

        location.setLocationName(name);
        location.setDescription(desc);
        location.setLatitude(lat);
        location.setLongitude(lng);

        if (bg != null && !bg.isEmpty()) location.setBackGroundImgUrl(upload(bg));
        if (imgs != null && !imgs.isEmpty()) location.setImgDetailsUrl(uploadList(imgs));

        return mapper.toDTO(locationRepository.save(location));
    }

    public void deleteLocation(Long id) {
        if (!locationRepository.existsById(id)) throw new RuntimeException("Location not found");
        locationRepository.deleteById(id);
    }

    public List<LocationResponseDTO> getAllLocations() {
        return locationRepository.findAll().stream().map(mapper::toDTO).toList();
    }

    public LocationResponseDTO getLocationById(Long id) {
        return mapper.toDTO(locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found")));
    }

    public List<LocationResponseDTO> findLocationsByMaterial(Long materialId) {
        return locationRepository.findByMaterials_Id(materialId).stream().map(mapper::toDTO).toList();
    }

    public List<LocationResponseDTO> findLocationsByDayOfWeek(String day) {
        try {
            DayOfWeek enumDay = DayOfWeek.valueOf(day.toUpperCase());
            return locationRepository.findByOpeningSchedules_DayOfWeek(enumDay)
                    .stream()
                    .map(mapper::toDTO)
                    .toList();
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid day of week: " + day);
        }
    }


    public List<LocationResponseDTO> findNearbyLocations(double lat, double lng, double radiusKm) {
        return locationRepository.findAll().stream()
                .filter(loc -> haversine(lat, lng, loc.getLatitude(), loc.getLongitude()) <= radiusKm)
                .map(mapper::toDTO).toList();
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        double a = Math.pow(Math.sin(dLat / 2), 2)
                + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
    }

    @Transactional
    public Locations addEmployeeToLocation(Long employeeId, Long locationId) {
        Locations locations = locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found"));

        List<Long> employeeList = locations.getEmployeeId();
        if (employeeList == null) {
            employeeList = new ArrayList<>();
        }
        if (!employeeList.contains(employeeId)) {
            employeeList.add(employeeId);
            locations.setEmployeeId(employeeList);
        }
        return locationRepository.save(locations);
    }

    private String upload(MultipartFile file) throws IOException {
        return file != null && !file.isEmpty() ? cloudinaryService.uploadFileLocation(file) : null;
    }

    private List<String> uploadList(List<MultipartFile> files) throws IOException {
        return (files != null && !files.isEmpty()) ? cloudinaryService.uploadMultipleFilesLocations(files) : new ArrayList<>();
    }

    @Transactional
    public void assignMaterialsToLocation(Long locationId, List<Long> materialIds) {
        Locations location = getLocationOrThrow(locationId);
        List<Materials> materials = materialRepository.findAllById(materialIds);
        location.setMaterials(materials);
        locationRepository.save(location);
    }

    @Transactional
    public void updateMaterialsForLocation(Long locationId, List<Long> materialIds) {
        assignMaterialsToLocation(locationId, materialIds); // logic giống nhau
    }

    @Transactional
    public void removeAllMaterials(Long locationId) {
        Locations location = getLocationOrThrow(locationId);
        location.getMaterials().clear();
        locationRepository.save(location);
    }

    @Transactional
    public void addOpeningSchedule(Long locationId, String dayOfWeekStr, List<TimeSlotDTO> timeSlots) {
        Locations location = getLocationOrThrow(locationId);
        DayOfWeek dayOfWeek = DayOfWeek.valueOf(dayOfWeekStr.toUpperCase());

        OpeningSchedule schedule = OpeningSchedule.builder()
                .dayOfWeek(dayOfWeek)
                .location(location)
                .timeSlots(timeSlots.stream()
                        .map(ts -> TimeSlot.builder()
                                .startTime(LocalTime.parse(ts.getStartTime()))
                                .endTime(LocalTime.parse(ts.getEndTime()))
                                .build())
                        .toList())
                .build();

        schedule.getTimeSlots().forEach(slot -> slot.setOpeningSchedule(schedule));
        location.getOpeningSchedules().add(schedule);
        locationRepository.save(location);
    }

    @Transactional
    public void updateOpeningSchedule(Long locationId, String dayOfWeekStr, List<TimeSlotDTO> timeSlots) {
        Locations location = getLocationOrThrow(locationId);
        DayOfWeek dayOfWeek = DayOfWeek.valueOf(dayOfWeekStr.toUpperCase());

        location.getOpeningSchedules().removeIf(s -> s.getDayOfWeek() == dayOfWeek);

        addOpeningSchedule(locationId, dayOfWeekStr, timeSlots);
    }

    @Transactional
    public void deleteSchedule(Long locationId, String dayOfWeekStr) {
        Locations location = getLocationOrThrow(locationId);
        DayOfWeek dayOfWeek = DayOfWeek.valueOf(dayOfWeekStr.toUpperCase());

        location.getOpeningSchedules().removeIf(s -> s.getDayOfWeek() == dayOfWeek);
        locationRepository.save(location);
    }

    private Locations getLocationOrThrow(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found"));
    }


}


