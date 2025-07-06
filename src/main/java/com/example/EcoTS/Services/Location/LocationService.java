package com.example.EcoTS.Services.Location;

import com.example.EcoTS.DTOs.Request.Location.*;
import com.example.EcoTS.DTOs.Response.Location.LocationResponseDTO;
import com.example.EcoTS.DTOs.Response.Location.LocationWithDistanceDTO;
import com.example.EcoTS.DTOs.Response.Location.TimeSlotDTO;
import com.example.EcoTS.Enum.DayOfWeek;
import com.example.EcoTS.Models.*;
import com.example.EcoTS.Repositories.*;

import com.example.EcoTS.Services.CloudinaryService.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.*;

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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationService {


    private final LocationRepository locationRepository;
    private final SponsorRepository sponsorRepository;
    private final MaterialRepository materialRepository;
    private final OpeningScheduleRepository scheduleRepository;
    private final TimeSlotRepository timeSlotRepository;
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
    public void updateMaterialsForLocation(Long locationId, List<Long> materialIds) {
        Locations location = locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found"));
        List<Materials> newMaterials = materialRepository.findAllById(materialIds);
        location.setMaterials(newMaterials);
        locationRepository.save(location);
    }

    @Transactional
    public void removeAllMaterials(Long locationId) {
        Locations location = locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found"));
        location.setMaterials(new ArrayList<>());
        locationRepository.save(location);
    }

    @Transactional
    public LocationResponseDTO addOpeningSchedule(AddScheduleRequest request) {
        Locations location = locationRepository.findById(request.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found with ID: " + request.getLocationId()));

        DayOfWeek day = DayOfWeek.valueOf(request.getDayOfWeek().toUpperCase());

        // Check if a schedule for this day already exists for this location
        boolean scheduleExists = location.getOpeningSchedules().stream()
                .anyMatch(s -> s.getDayOfWeek() == day);
        if (scheduleExists) {
            throw new RuntimeException("An opening schedule for " + day + " already exists at this location. Use update if you want to modify.");
        }

        OpeningSchedule schedule = OpeningSchedule.builder()
                .location(location)
                .dayOfWeek(day)
                .timeSlots(new ArrayList<>()) // Initialize for new time slots
                .build();

        // Add initial time slots, performing overlap check
        List<TimeSlot> newTimeSlots = new ArrayList<>();
        for (UpdateTimeSlotRequest slotDTO : request.getTimeSlots()) {
            LocalTime startTime = LocalTime.parse(slotDTO.getStartTime());
            LocalTime endTime = LocalTime.parse(slotDTO.getEndTime());

            // Check for overlaps within the new time slots being added
            for (TimeSlot existingSlot : newTimeSlots) {
                if (startTime.isBefore(existingSlot.getEndTime()) && endTime.isAfter(existingSlot.getStartTime())) {
                    throw new RuntimeException("New time slots overlap detected: " + slotDTO.getStartTime() + "-" + slotDTO.getEndTime() +
                            " overlaps with " + existingSlot.getStartTime() + "-" + existingSlot.getEndTime());
                }
            }

            TimeSlot newSlot = TimeSlot.builder()
                    .startTime(startTime)
                    .endTime(endTime)
                    .openingSchedule(schedule)
                    .build();
            newTimeSlots.add(newSlot);
        }
        schedule.setTimeSlots(newTimeSlots);

        location.getOpeningSchedules().add(schedule); // Link new schedule to location
        scheduleRepository.save(schedule); // Save schedule (and time slots via cascade)
        locationRepository.save(location); // Ensure location relationship is updated

        return mapper.toDTO(location);
    }

    @Transactional
    public LocationResponseDTO updateOpeningScheduleDayOfWeek(UpdateExistingScheduleRequest request) {
        OpeningSchedule schedule = scheduleRepository.findById(request.getScheduleId())
                .orElseThrow(() -> new RuntimeException("Opening schedule not found with ID: " + request.getScheduleId()));

        if (request.getDayOfWeek() != null) {
            DayOfWeek newDay = DayOfWeek.valueOf(request.getDayOfWeek().toUpperCase());

            // Check if another schedule for this new day already exists at the same location
            Locations location = schedule.getLocation();
            boolean dayExistsForOtherSchedule = location.getOpeningSchedules().stream()
                    .filter(s -> !s.getId().equals(schedule.getId())) // Exclude the current schedule
                    .anyMatch(s -> s.getDayOfWeek() == newDay);

            if (dayExistsForOtherSchedule) {
                throw new RuntimeException("An opening schedule for " + newDay + " already exists at this location.");
            }

            schedule.setDayOfWeek(newDay);
            scheduleRepository.save(schedule); // Save the updated schedule
        }
        return mapper.toDTO(schedule.getLocation());
    }

    @Transactional
    public LocationResponseDTO addTimeSlotsToSchedule(ManageTimeSlotRequest request) {
        OpeningSchedule schedule = scheduleRepository.findById(request.getScheduleId())
                .orElseThrow(() -> new RuntimeException("Opening schedule not found with ID: " + request.getScheduleId()));

        // Ensure timeSlots list is initialized
        if (schedule.getTimeSlots() == null) {
            schedule.setTimeSlots(new ArrayList<>());
        }

        List<TimeSlot> currentScheduleTimeSlots = new ArrayList<>(schedule.getTimeSlots());
        List<TimeSlot> timeSlotsToAdd = new ArrayList<>();

        for (UpdateTimeSlotRequest slotDTO : request.getTimeSlots()) {
            if (slotDTO.getId() != null && slotDTO.getId() != 0) {
                throw new IllegalArgumentException("Cannot add a time slot with an existing ID. Use update for existing slots.");
            }

            LocalTime startTime = LocalTime.parse(slotDTO.getStartTime());
            LocalTime endTime = LocalTime.parse(slotDTO.getEndTime());

            // Check for overlaps with existing slots in the schedule and newly added slots in this request
            for (TimeSlot existingSlot : currentScheduleTimeSlots) {
                if (startTime.isBefore(existingSlot.getEndTime()) && endTime.isAfter(existingSlot.getStartTime())) {
                    throw new RuntimeException("Time slot overlap detected: " + slotDTO.getStartTime() + "-" + slotDTO.getEndTime() +
                            " overlaps with " + existingSlot.getStartTime() + "-" + existingSlot.getEndTime());
                }
            }
            for (TimeSlot newlyAddedSlot : timeSlotsToAdd) { // Check against other slots being added in this request
                if (startTime.isBefore(newlyAddedSlot.getEndTime()) && endTime.isAfter(newlyAddedSlot.getStartTime())) {
                    throw new RuntimeException("Time slot overlap detected: " + slotDTO.getStartTime() + "-" + slotDTO.getEndTime() +
                            " overlaps with " + newlyAddedSlot.getStartTime() + "-" + newlyAddedSlot.getEndTime() + " (within current batch)");
                }
            }

            TimeSlot newSlot = TimeSlot.builder()
                    .startTime(startTime)
                    .endTime(endTime)
                    .openingSchedule(schedule)
                    .build();
            timeSlotsToAdd.add(newSlot);
        }

        schedule.getTimeSlots().addAll(timeSlotsToAdd); // Add all new slots to the schedule's list
        scheduleRepository.save(schedule); // Save schedule (time slots will be cascaded)

        return mapper.toDTO(schedule.getLocation());
    }

    @Transactional
    public LocationResponseDTO updateTimeSlotsInSchedule(ManageTimeSlotRequest request) {
        OpeningSchedule schedule = scheduleRepository.findById(request.getScheduleId())
                .orElseThrow(() -> new RuntimeException("Opening schedule not found with ID: " + request.getScheduleId()));

        Map<Long, TimeSlot> existingTimeSlotsMap = schedule.getTimeSlots().stream()
                .filter(ts -> ts.getId() != null)
                .collect(Collectors.toMap(TimeSlot::getId, ts -> ts));

        List<TimeSlot> updatedSlots = new ArrayList<>();

        for (UpdateTimeSlotRequest slotDTO : request.getTimeSlots()) {
            if (slotDTO.getId() == null || slotDTO.getId() == 0) {
                throw new IllegalArgumentException("Time slot ID must be provided for update operations.");
            }

            TimeSlot slotToUpdate = existingTimeSlotsMap.get(slotDTO.getId());
            if (slotToUpdate == null) {
                throw new RuntimeException("Time slot not found with ID: " + slotDTO.getId() + " in schedule " + request.getScheduleId());
            }

            LocalTime newStartTime = LocalTime.parse(slotDTO.getStartTime());
            LocalTime newEndTime = LocalTime.parse(slotDTO.getEndTime());

            // Check for overlaps with other existing slots in the schedule
            // Filter out the slot currently being updated from the overlap check
            List<TimeSlot> otherExistingSlots = schedule.getTimeSlots().stream()
                    .filter(ts -> !ts.getId().equals(slotToUpdate.getId()))
                    .collect(Collectors.toList());

            for (TimeSlot existingSlot : otherExistingSlots) {
                if (newStartTime.isBefore(existingSlot.getEndTime()) && newEndTime.isAfter(existingSlot.getStartTime())) {
                    throw new RuntimeException("Time slot overlap detected: " + slotDTO.getStartTime() + "-" + slotDTO.getEndTime() +
                            " overlaps with " + existingSlot.getStartTime() + "-" + existingSlot.getEndTime());
                }
            }

            slotToUpdate.setStartTime(newStartTime);
            slotToUpdate.setEndTime(newEndTime);
            updatedSlots.add(slotToUpdate); // Collect updated slots, though not strictly needed for cascade update
        }
        // Save the schedule; updated slots will be persisted via transactional context
        scheduleRepository.save(schedule);
        return mapper.toDTO(schedule.getLocation());
    }

    @Transactional
    public LocationResponseDTO removeTimeSlotsFromSchedule(Long scheduleId, List<Long> timeSlotIds) {
        OpeningSchedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Opening schedule not found with ID: " + scheduleId));

        if (schedule.getTimeSlots() == null) {
            schedule.setTimeSlots(new ArrayList<>());
        }

        List<TimeSlot> slotsToRemove = schedule.getTimeSlots().stream()
                .filter(ts -> timeSlotIds.contains(ts.getId()))
                .collect(Collectors.toList());

        if (slotsToRemove.isEmpty()) {
            throw new RuntimeException("No time slots found with provided IDs to remove from schedule " + scheduleId);
        }

        schedule.getTimeSlots().removeAll(slotsToRemove); // Remove from collection
        timeSlotRepository.deleteAll(slotsToRemove); // Explicitly delete from DB (orphanRemoval would also work)

        scheduleRepository.save(schedule); // Save schedule to persist changes in collection
        return mapper.toDTO(schedule.getLocation());
    }

    // --- Các phương thức đã có (giữ nguyên) ---
    // Phương thức deleteScheduleByDay vẫn có thể giữ lại nếu bạn muốn một API riêng chỉ để xóa toàn bộ lịch trình của một ngày.
    @Transactional
    public void deleteSchedule(Long locationId, String dayOfWeek) {
        Locations location = locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found"));

        DayOfWeek day = DayOfWeek.valueOf(dayOfWeek.toUpperCase());
        List<OpeningSchedule> schedules = location.getOpeningSchedules();

        // Sử dụng Iterator hoặc tạo list tạm để tránh ConcurrentModificationException
        List<OpeningSchedule> schedulesToRemove = new ArrayList<>();
        for (OpeningSchedule s : schedules) {
            if (s.getDayOfWeek() == day) {
                schedulesToRemove.add(s);
            }
        }

        for (OpeningSchedule s : schedulesToRemove) {
            location.getOpeningSchedules().remove(s); // Xóa khỏi danh sách của location
            scheduleRepository.delete(s); // Xóa khỏi database (orphanRemoval sẽ xóa TimeSlots)
        }

        locationRepository.save(location); // Lưu lại location sau khi cập nhật danh sách schedule
    }

    @Transactional
    public List<LocationResponseDTO> searchLocationsByMultipleCriteria(
            Long materialId,
            String dayOfWeek,
            LocalTime startTime,
            LocalTime endTime,
            Double latitude,
            Double longitude,
            Double radiusKm) {

        List<Locations> all = locationRepository.findAll();

        return all.stream()
                .filter(loc -> {
                    // Filter by material
                    if (materialId != null) {
                        boolean hasMaterial = loc.getMaterials().stream()
                                .anyMatch(mat -> mat.getId().equals(materialId));
                        if (!hasMaterial) return false;
                    }

                    // Filter by opening schedule day and time
                    if (dayOfWeek != null) {
                        try {
                            DayOfWeek day = DayOfWeek.valueOf(dayOfWeek.toUpperCase());

                            Optional<OpeningSchedule> optionalSchedule = loc.getOpeningSchedules().stream()
                                    .filter(s -> s.getDayOfWeek() == day)
                                    .findFirst();

                            if (optionalSchedule.isEmpty()) return false;

                            if (startTime != null && endTime != null) {
                                boolean isOpenDuringTime = optionalSchedule.get().getTimeSlots().stream()
                                        .anyMatch(slot ->
                                                !slot.getStartTime().isAfter(startTime) &&
                                                        !slot.getEndTime().isBefore(endTime)
                                        );
                                if (!isOpenDuringTime) return false;
                            }

                        } catch (IllegalArgumentException e) {
                            return false;
                        }
                    }

                    // Filter by radius
                    if (latitude != null && longitude != null && radiusKm != null) {
                        double distance = haversine(latitude, longitude, loc.getLatitude(), loc.getLongitude());
                        if (distance > radiusKm) return false;
                    }

                    return true;
                })
                .map(mapper::toDTO)
                .toList();
    }
    @Transactional
    public List<LocationResponseDTO> getLocationsBySponsorId(Long sponsorId) {
        sponsorRepository.findById(sponsorId)
                .orElseThrow(() -> new ResourceNotFoundException("Sponsor không tồn tại: " + sponsorId));

        List<Locations> entities = locationRepository.findBySponsorId(sponsorId);

        return entities.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }
}


