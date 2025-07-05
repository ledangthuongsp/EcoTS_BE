package com.example.EcoTS.Services.Location;

import com.example.EcoTS.DTOs.Request.Location.LocationDTO;
import com.example.EcoTS.DTOs.Request.Location.UpdateScheduleRequest;
import com.example.EcoTS.DTOs.Request.Location.UpdateTimeSlotRequest;
import com.example.EcoTS.DTOs.Response.Location.LocationResponseDTO;
import com.example.EcoTS.DTOs.Response.Location.TimeSlotDTO;
import com.example.EcoTS.Enum.DayOfWeek;
import com.example.EcoTS.Models.*;
import com.example.EcoTS.Repositories.*;

import com.example.EcoTS.Services.CloudinaryService.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<LocationResponseDTO> findLocationsByMaterial(Long materialId) {
        return locationRepository.findByMaterials_Id(materialId).stream().map(mapper::toDTO).toList();
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
    public LocationResponseDTO upsertOpeningSchedule(UpdateScheduleRequest request) {
        // 1. Tìm Location
        Locations location = locationRepository.findById(request.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found with ID: " + request.getLocationId()));

        // 2. Tìm hoặc Tạo OpeningSchedule
        OpeningSchedule schedule;
        if (request.getScheduleId() != null) { // Nếu có scheduleId, tìm schedule hiện có
            schedule = scheduleRepository.findById(request.getScheduleId())
                    .orElseThrow(() -> new RuntimeException("Opening schedule not found with ID: " + request.getScheduleId()));
            // Nếu dayOfWeek được cung cấp trong request và khác với ngày hiện tại của schedule
            if (request.getDayOfWeek() != null && !schedule.getDayOfWeek().name().equalsIgnoreCase(request.getDayOfWeek())) {
                schedule.setDayOfWeek(DayOfWeek.valueOf(request.getDayOfWeek().toUpperCase()));
            }
        } else { // Nếu không có scheduleId, kiểm tra xem đã có schedule cho ngày này chưa
            if (request.getDayOfWeek() == null) {
                throw new IllegalArgumentException("Day of week must be provided to create a new schedule.");
            }
            DayOfWeek newDay = DayOfWeek.valueOf(request.getDayOfWeek().toUpperCase());

            // Tìm schedule hiện có cho ngày này tại location này
            schedule = location.getOpeningSchedules().stream()
                    .filter(s -> s.getDayOfWeek() == newDay)
                    .findFirst()
                    .orElse(null);

            if (schedule == null) { // Nếu chưa có, tạo mới
                schedule = OpeningSchedule.builder()
                        .location(location)
                        .dayOfWeek(newDay)
                        .timeSlots(new ArrayList<>()) // Khởi tạo danh sách TimeSlots rỗng
                        .build();
                // Liên kết schedule mới với location để đảm bảo nó được quản lý bởi JPA
                location.getOpeningSchedules().add(schedule);
            }
            // Nếu đã tồn tại, chúng ta sẽ cập nhật schedule đó
        }

        // Đảm bảo TimeSlots của schedule không null để tránh NullPointerException
        if (schedule.getTimeSlots() == null) {
            schedule.setTimeSlots(new ArrayList<>());
        }

        // 3. Xử lý TimeSlots: Xác định các TimeSlot để xóa, cập nhật, và thêm mới
        // Danh sách ID TimeSlot hiện tại trong DB cho schedule này
        Map<Long, TimeSlot> existingTimeSlotsMap = schedule.getTimeSlots().stream()
                .filter(ts -> ts.getId() != null) // Chỉ lấy các TimeSlot đã có ID
                .collect(Collectors.toMap(TimeSlot::getId, ts -> ts));

        // Danh sách ID TimeSlot nhận được từ request
        List<Long> requestedTimeSlotIds = request.getTimeSlots().stream()
                .filter(ts -> ts.getId() != null && ts.getId() != 0)
                .map(UpdateTimeSlotRequest::getId)
                .collect(Collectors.toList());

        // Lọc ra các TimeSlot cần xóa (có trong DB nhưng không có trong request)
        List<TimeSlot> timeSlotsToDelete = existingTimeSlotsMap.values().stream()
                .filter(ts -> !requestedTimeSlotIds.contains(ts.getId()))
                .collect(Collectors.toList());

        // Xóa các TimeSlot khỏi schedule và database
        if (!timeSlotsToDelete.isEmpty()) {
            schedule.getTimeSlots().removeAll(timeSlotsToDelete);
            // JPA với orphanRemoval = true sẽ tự động xóa các TimeSlot bị remove khỏi collection
            // timeSlotRepository.deleteAll(timeSlotsToDelete); // Không cần gọi tường minh nếu orphanRemoval = true
        }

        // Danh sách TimeSlot mới sẽ được gán cho schedule
        List<TimeSlot> newOrUpdatedTimeSlots = new ArrayList<>();

        // Duyệt qua các TimeSlot từ request để cập nhật hoặc thêm mới
        for (UpdateTimeSlotRequest slotDTO : request.getTimeSlots()) {
            LocalTime startTime = LocalTime.parse(slotDTO.getStartTime());
            LocalTime endTime = LocalTime.parse(slotDTO.getEndTime());

            // 4. Logic kiểm tra trùng lặp và đè lên nhau trong một DayOfWeek
            // Cần kiểm tra với tất cả các TimeSlot đã tồn tại (trừ chính nó nếu là cập nhật)
            // và các TimeSlot mới được thêm vào trong cùng request này.

            // Tạo một danh sách tổng hợp các TimeSlot để kiểm tra, bao gồm cả các slot cũ chưa bị xóa
            // và các slot mới được thêm vào trong cùng request này.
            List<TimeSlot> allSlotsForOverlapCheck = new ArrayList<>(newOrUpdatedTimeSlots); // Các slot đã được xử lý trong request này
            allSlotsForOverlapCheck.addAll(schedule.getTimeSlots().stream() // Các slot hiện có trong schedule (chưa bị xóa)
                    .filter(ts -> !timeSlotsToDelete.contains(ts)) // Loại bỏ các slot sẽ bị xóa
                    .collect(Collectors.toList()));


            for (TimeSlot existingSlot : allSlotsForOverlapCheck) {
                // Điều kiện để so sánh: không phải là cùng một slot đang được cập nhật
                boolean isSameSlot = (slotDTO.getId() != null && slotDTO.getId().equals(existingSlot.getId()));
                if (!isSameSlot && (startTime.isBefore(existingSlot.getEndTime()) && endTime.isAfter(existingSlot.getStartTime()))) {
                    throw new RuntimeException("Time slots overlap detected: " + slotDTO.getStartTime() + "-" + slotDTO.getEndTime() +
                            " overlaps with " + existingSlot.getStartTime() + "-" + existingSlot.getEndTime());
                }
            }

            if (slotDTO.getId() != null && slotDTO.getId() != 0 && existingTimeSlotsMap.containsKey(slotDTO.getId())) {
                // Cập nhật TimeSlot đã tồn tại
                TimeSlot slotToUpdate = existingTimeSlotsMap.get(slotDTO.getId());
                slotToUpdate.setStartTime(startTime);
                slotToUpdate.setEndTime(endTime);
                newOrUpdatedTimeSlots.add(slotToUpdate);
            } else {
                // Thêm mới TimeSlot
                TimeSlot newSlot = TimeSlot.builder()
                        .startTime(startTime)
                        .endTime(endTime)
                        .openingSchedule(schedule) // Gán mối quan hệ
                        .build();
                newOrUpdatedTimeSlots.add(newSlot);
            }
        }
        // Gán danh sách TimeSlot đã được xử lý (thêm mới/cập nhật) vào schedule
        schedule.setTimeSlots(newOrUpdatedTimeSlots);

        // 5. Lưu OpeningSchedule và Location
        // JPA sẽ xử lý các TimeSlot mới/cập nhật/xóa do CascadeType.ALL và orphanRemoval
        scheduleRepository.save(schedule); // Lưu schedule (và các timeSlot liên quan)
        locationRepository.save(location); // Lưu location để đảm bảo mối quan hệ ngược lại (nếu schedule là mới)

        return mapper.toDTO(location);
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
}


