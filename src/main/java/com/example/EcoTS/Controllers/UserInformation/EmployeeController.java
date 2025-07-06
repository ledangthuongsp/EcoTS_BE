package com.example.EcoTS.Controllers.UserInformation;

import com.example.EcoTS.DTOs.Response.Location.LocationResponseDTO;
import com.example.EcoTS.DTOs.Response.User.UsersDTO;
import com.example.EcoTS.Models.Locations;
import com.example.EcoTS.Models.Users;
import com.example.EcoTS.Repositories.LocationRepository;
import com.example.EcoTS.Services.Location.LocationMapper;
import com.example.EcoTS.Services.UserService.EmployeeService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.hibernate.validator.constraints.pl.REGON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.stream.Location;
import java.util.List;
import java.util.Locale;

@RestController
@CrossOrigin
@RequestMapping("/employee")
@Tag(name = "Everything about Employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private LocationMapper locationMapper;
    @PostMapping("/add-to-location")
    public Locations addEmployeeToLocation (@RequestParam Long employeeId, @RequestParam Long locationId)
    {
        return employeeService.addEmployeeToLocation(employeeId, locationId);
    }
    @GetMapping("/get-all-employee")

    public List<UsersDTO> getAllEmployee()
    {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/get-location-by-employee-id")
    public LocationResponseDTO getLocationByEmployeeId(Long employeeId) {
        // tìm location đầu tiên (hoặc tùy logic) chứa employeeId
        Locations loc = locationRepository.findAll().stream()
                .filter(l -> l.getEmployeeId().contains(employeeId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No location assigned to employee " + employeeId));

        return locationMapper.toDTO(loc);
    }
}
