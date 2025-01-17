package com.example.EcoTS.Controllers.Sponsor;

import com.example.EcoTS.Models.Sponsor;
import com.example.EcoTS.Services.Sponsor.SponsorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sponsor")
@CrossOrigin
@Tag(name = "API For Sponsor")
public class SponsorController {

    @Autowired
    private SponsorService sponsorService;

    // API tạo mới sponsor
    @PostMapping("/create")
    public ResponseEntity<Sponsor> createSponsor(@RequestBody Sponsor sponsor) {
        Sponsor createdSponsor = sponsorService.createSponsor(sponsor);
        return ResponseEntity.ok(createdSponsor);
    }

    // API cập nhật sponsor
    @PutMapping("/update/{id}")
    public ResponseEntity<Sponsor> updateSponsor(@PathVariable Long id, @RequestBody Sponsor sponsorDetails) {
        Sponsor updatedSponsor = sponsorService.updateSponsor(id, sponsorDetails);
        return ResponseEntity.ok(updatedSponsor);
    }

    // API lấy thông tin sponsor theo id
    @GetMapping("/{id}")
    public ResponseEntity<Sponsor> getSponsorById(@PathVariable Long id) {
        Sponsor sponsor = sponsorService.getSponsorById(id);
        return ResponseEntity.ok(sponsor);
    }

    // API xóa sponsor
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteSponsor(@PathVariable Long id) {
        sponsorService.deleteSponsor(id);
        return ResponseEntity.noContent().build();
    }
}
