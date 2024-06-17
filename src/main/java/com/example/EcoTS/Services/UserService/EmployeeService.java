package com.example.EcoTS.Services.UserService;

import com.example.EcoTS.Enum.Roles;
import com.example.EcoTS.Models.Locations;
import com.example.EcoTS.Models.Users;
import com.example.EcoTS.Repositories.LocationRepository;
import com.example.EcoTS.Repositories.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LocationRepository locationRepository;

    @Transactional
    public Locations addEmployeeToLocation (Long employeeId, Long locationId)
    {
        Locations locations = locationRepository.findById(locationId).orElseThrow();
        List<Long> employeeList;
        if(locations.getEmployeeId() == null)
        {
            employeeList = new ArrayList<>();
        }
        else
        {
            employeeList = locations.getEmployeeId();
        }
        employeeList.add(employeeId);
        locations.setEmployeeId(employeeList);
        return locationRepository.save(locations);
    }
    @Transactional
    public Locations getLocationByEmployeeId(Long employeeId)
    {
        Locations location = locationRepository.findByEmployeeId(employeeId).orElseThrow();
        // Initialize lazy-loaded collections or attributes
        if (location.getEmployeeId() != null) {
            location.getEmployeeId().size(); // force initialization
        }
        return location;

    }
    @Transactional
    public List<Users> getAllEmployee()
    {
        return userRepository.findByRole(Roles.EMPLOYEE.toString());
    }
}
