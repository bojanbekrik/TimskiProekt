package com.example.parkup.web;

import com.example.parkup.data.entities.ParkingZone;
import com.example.parkup.service.ParkingZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ParkingZoneController {
    private final ParkingZoneService parkingZoneService;

    @Autowired
    public ParkingZoneController(ParkingZoneService parkingZoneService) {
        this.parkingZoneService = parkingZoneService;
    }

    @GetMapping("/parkingZone")
    public List<ParkingZone> getAllParkingZone() {
        return parkingZoneService.getAllParkingZones();
    }

}
