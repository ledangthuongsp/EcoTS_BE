package com.example.EcoTS.Services.Location;

import com.example.EcoTS.DTOs.Response.Location.*;
import com.example.EcoTS.Models.*;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LocationMapper {

    public LocationResponseDTO toDTO(Locations location) {
        return LocationResponseDTO.builder()
                .id(location.getId())
                .locationName(location.getLocationName())
                .description(location.getDescription())
                .typeOfLocation(location.getTypeOfLocation())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .backGroundImgUrl(location.getBackGroundImgUrl())
                .imgDetailsUrl(location.getImgDetailsUrl())
                .materials(mapMaterials(location.getMaterials()))
                .openingSchedules(mapOpeningSchedules(location.getOpeningSchedules()))
                .sponsor(mapSponsor(location.getSponsor()))
                .build();
    }

    private List<MaterialDTO> mapMaterials(List<Materials> materials) {
        return materials == null ? null : materials.stream().map(material ->
                MaterialDTO.builder()
                        .id(material.getId())
                        .name(material.getName())
                        .pointsPerKg(material.getPointsPerKg())
                        .co2SavedPerKg(material.getCo2SavedPerKg())
                        .type(material.getType())
                        .build()
        ).collect(Collectors.toList());
    }

    private List<OpeningScheduleDTO> mapOpeningSchedules(List<OpeningSchedule> schedules) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm"); // hoặc định dạng bạn muốn

        return schedules == null ? null : schedules.stream().map(schedule ->
                OpeningScheduleDTO.builder()
                        .dayOfWeek(schedule.getDayOfWeek().name()) // enum → String
                        .timeSlots(schedule.getTimeSlots().stream().map(slot ->
                                TimeSlotDTO.builder()
                                        .startTime(slot.getStartTime().format(formatter)) //  LocalDateTime → String
                                        .endTime(slot.getEndTime().format(formatter))
                                        .build()
                        ).collect(Collectors.toList()))
                        .build()
        ).collect(Collectors.toList());
    }


    private SponsorDTO mapSponsor(Sponsor sponsor) {
        if (sponsor == null) return null;
        return SponsorDTO.builder()
                .id(sponsor.getId())
                .companyName(sponsor.getCompanyName())
                .avatarUrl(sponsor.getAvatarUrl())
                .build();
    }
}
