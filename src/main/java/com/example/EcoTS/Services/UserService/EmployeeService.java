package com.example.EcoTS.Services.UserService;

import com.example.EcoTS.Models.Locations;
import com.example.EcoTS.Repositories.LocationRepository;
import com.example.EcoTS.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LocationRepository locationRepository;

    public Locations addEmployeeToLocation (Long userId, Long locationId)
    {
        Locations locations = locationRepository.findById(locationId).orElseThrow();
        List<Long> employeeList;
        if(locations.getUserId() == null)
        {
            employeeList = new ArrayList<>();
        }
        else
        {
            employeeList = locations.getUserId();
        }
        employeeList.add(userId);
        locations.setUserId(employeeList);
        return locationRepository.save(locations);
    }
}
