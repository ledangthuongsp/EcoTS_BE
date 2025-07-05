package com.example.EcoTS.Mappers;
import com.example.EcoTS.DTOs.Response.Material.MaterialResponseDTO;
import com.example.EcoTS.Models.Materials;
import org.springframework.stereotype.Component;

@Component
public class MaterialMapper {
    public MaterialResponseDTO toDTO(Materials material) {
        MaterialResponseDTO dto = new MaterialResponseDTO();
        dto.setId(material.getId());
        dto.setName(material.getName());
        dto.setPointsPerKg(material.getPointsPerKg());
        dto.setCo2SavedPerKg(material.getCo2SavedPerKg());
        dto.setType(material.getType());
        return dto;
    }
}
