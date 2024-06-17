package com.example.EcoTS.Controllers.UserInformation;

import com.example.EcoTS.DTOs.Response.User.UsersDTO;
import com.example.EcoTS.Models.Locations;
import com.example.EcoTS.Models.Users;
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
    public Locations getLocationByEmployeeId(@RequestParam Long employeeId)
    {
        return employeeService.getLocationByEmployeeId(employeeId);
    }
}
