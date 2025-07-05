package com.example.EcoTS.Controllers.Material;


import com.example.EcoTS.DTOs.Response.Material.MaterialResponseDTO;
import com.example.EcoTS.Mappers.MaterialMapper;
import com.example.EcoTS.Models.Materials;
import com.example.EcoTS.Repositories.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;

@RestController
@CrossOrigin
@Tag(name ="Material CRUD", description = "APIs for adding, updating, and deleting materials. Used to assign points and CO2 savings.")
@RequestMapping("/materials")
public class MaterialController {

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private MaterialMapper materialMapper;

    @GetMapping("/get-all-materials")
    public ResponseEntity<List<MaterialResponseDTO>> getAllMaterials() {
        List<MaterialResponseDTO> materialDTOs = materialRepository.findAll().stream()
                .map(materialMapper::toDTO)
                .toList();
        return ResponseEntity.ok(materialDTOs);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addMaterial(@RequestBody Materials material) {
        materialRepository.save(material);
        return new ResponseEntity<>("Material added successfully", HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteMaterial(@PathVariable Long id) {
        materialRepository.deleteById(id);
        return new ResponseEntity<>("Material deleted successfully", HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateMaterial(@RequestParam Long id,
                                                 @RequestParam double pointPerKg,
                                                 @RequestParam double saveCo2perKg) {
        Materials existingMaterial = materialRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Material not found"));
        existingMaterial.setPointsPerKg(pointPerKg);
        existingMaterial.setCo2SavedPerKg(saveCo2perKg);
        materialRepository.save(existingMaterial);
        return new ResponseEntity<>("Material updated successfully", HttpStatus.OK);
    }
}


