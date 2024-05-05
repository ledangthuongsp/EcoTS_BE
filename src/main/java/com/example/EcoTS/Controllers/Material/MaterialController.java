package com.example.EcoTS.Controllers.Material;


import com.example.EcoTS.Models.Materials;
import com.example.EcoTS.Repositories.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin
@Tag(name ="Material CRUD", description = "this apis for changing and updating, adding material. About optional for save co2 and give point for user")
@RequestMapping("/materials")
public class MaterialController {

    @Autowired
    private MaterialRepository materialRepository;

    // Thêm một vật liệu mới
    @PostMapping("/add")
    public ResponseEntity<String> addMaterial(@RequestBody Materials material) {
        materialRepository.save(material);
        return new ResponseEntity<>("Material added successfully", HttpStatus.CREATED);
    }

    // Xóa một vật liệu dựa trên id
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteMaterial(@PathVariable Long id) {
        materialRepository.deleteById(id);
        return new ResponseEntity<>("Material deleted successfully", HttpStatus.OK);
    }

    // Cập nhật thông tin về một vật liệu
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateMaterial(@PathVariable Long id, @RequestBody Materials updatedMaterial) {
        Materials existingMaterial = materialRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Material not found"));

        existingMaterial.setName(updatedMaterial.getName());
        existingMaterial.setPointsPerKg(updatedMaterial.getPointsPerKg());
        existingMaterial.setCo2SavedPerKg(updatedMaterial.getCo2SavedPerKg());

        materialRepository.save(existingMaterial);
        return new ResponseEntity<>("Material updated successfully", HttpStatus.OK);
    }
}

