package com.example.parkup.web;

import com.example.parkup.data.dto.ParkingZoneAdminDTO;
import com.example.parkup.data.entities.ParkingZone;
import com.example.parkup.service.ParkingZoneService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ParkingZoneController {
    private final ParkingZoneService parkingZoneService;

    public ParkingZoneController(ParkingZoneService parkingZoneService) {
        this.parkingZoneService = parkingZoneService;
    }

    @GetMapping({"/parking-zones"})
    public List<ParkingZone> getAllParkingZone() {
        return parkingZoneService.getAllParkingZones();
    }

    @GetMapping({"/parking-zones/{parkingZoneId}"})
    public ParkingZoneAdminDTO getParkingZone(@PathVariable int parkingZoneId) {
        ParkingZoneAdminDTO parkingZone = parkingZoneService.findById(parkingZoneId);
        if (parkingZone == null) {
            throw new RuntimeException("ParkingZone not found");
        }
        return parkingZone;
    }

    @GetMapping({"/parking-zones/names"})
    public List<String> getParkingZoneNames(){
        return this.parkingZoneService.getAllParkingZoneNames();
    }

    @GetMapping({"/parking-zones/names/{parkingZoneName}"})
    public ParkingZone getParkingZone(@PathVariable String parkingZoneName){
        return parkingZoneService.getParkingZoneByName(parkingZoneName);
    }

    @PostMapping({"/parking-zone"})
    public ParkingZone addParkingZone(@RequestBody ParkingZone parkingZone) {
        return this.parkingZoneService.addParkingZone(parkingZone);
    }

    @PostMapping("/parking-zones/name")
    public ParkingZone addParkingZoneName(@RequestBody String name){
        return this.parkingZoneService.addParkingZoneNameOnly(name);
    }

    @PutMapping({"/parking-zone/{parkingZoneId}"})
    public ParkingZoneAdminDTO updateParkingZone(@PathVariable int parkingZoneId, @RequestBody ParkingZoneAdminDTO parkingZone) {
        return this.parkingZoneService.updateParkingZone(parkingZoneId, parkingZone);
    }

    @DeleteMapping({"/parking-zone/{parkingZoneId}"})
    public void deleteParkingZone(@PathVariable int parkingZoneId) {
        this.parkingZoneService.deleteParkingZone(parkingZoneId);
    }
}
