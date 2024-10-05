package com.example.parkup.web;

import com.example.parkup.data.entities.ParkingSpace;
import com.example.parkup.service.ParkingSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ParkingSpaceController {

    private final ParkingSpaceService parkingSpaceService;

    @Autowired
    public ParkingSpaceController(ParkingSpaceService parkingSpaceService) {
        this.parkingSpaceService = parkingSpaceService;
    }

    @GetMapping("/parking-spaces")
    public List<ParkingSpace> getAllParkingSpaces() {
        return parkingSpaceService.getAllParkingSpaces();
    }

    @GetMapping("/parking-spaces/{parkingSpaceId}")
    public ParkingSpace getParkingSpace(@PathVariable int parkingSpaceId) {
        ParkingSpace parkingSpace = parkingSpaceService.findById(parkingSpaceId);
        if (parkingSpace == null) {
            throw new RuntimeException("ParkingSpace not found");
        }
        return parkingSpace;
    }

    @PostMapping("/parking-space")
    public ParkingSpace addParkingSpace(@RequestBody ParkingSpace parkingSpace) {
        return this.parkingSpaceService.addParkingSpace(parkingSpace);
    }

    @PutMapping("/parking-space/{parkingSpaceId}")
    public void updateParkingSpace(@PathVariable int parkingSpaceId, @RequestBody ParkingSpace parkingSpace) {
        this.parkingSpaceService.updateParkingSpace(parkingSpaceId, parkingSpace);
    }

    @DeleteMapping("/parking-space/{parkingSpaceId}")
    public void deleteParkingSpace(@PathVariable int parkingSpaceId) {
        this.parkingSpaceService.deleteParkingSpace(parkingSpaceId);
    }

}
