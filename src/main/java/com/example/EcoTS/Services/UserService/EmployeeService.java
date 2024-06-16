package com.example.EcoTS.Services.UserService;

import com.example.EcoTS.Models.Locations;
import com.example.EcoTS.Repositories.LocationRepository;
import com.example.EcoTS.Repositories.UserRepository;
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
}
