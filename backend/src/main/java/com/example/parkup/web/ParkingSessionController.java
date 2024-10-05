package com.example.parkup.web;

import com.example.parkup.data.entities.ParkingSession;
import com.example.parkup.service.ParkingSessionService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ParkingSessionController {
    private final ParkingSessionService parkingSessionService;

    public ParkingSessionController(ParkingSessionService parkingSessionService) {
        this.parkingSessionService = parkingSessionService;
    }

    @GetMapping({"/parking-sessions/{parkingZoneId}"})
    public List<ParkingSession> getAllParkingSessionsByParkingZone(@PathVariable Integer parkingZoneId) {
        return parkingSessionService.getAllParkingSessionsFromZone(parkingZoneId);
    }
    @GetMapping({"/parking-session"})
    public ParkingSession getParkingSession(){
        return parkingSessionService.getParkingSession();
    }

    @PostMapping({"/parking-session/{parkingZoneName}"})
    public ParkingSession startParkingSessionByParkingZone(@RequestParam String tablicka, @PathVariable String parkingZoneName) {
        return this.parkingSessionService.startParkingSession(tablicka,parkingZoneName);
    }

    @PutMapping("/parking-session/end")
    public ParkingSession endParkingSession(){
        return this.parkingSessionService.endParkingSession();
    }

    @PutMapping("/parking-session/verify/{parkingSessionId}")
    public ParkingSession verifyParkingSession(@PathVariable int parkingSessionId,@RequestParam String parkingSpaceName){
        return this.parkingSessionService.verifyParkingSession(parkingSessionId,parkingSpaceName);
    }
    @GetMapping("/parking-session/end/calculate")
    public int calculateParkingSession(){
        return this.parkingSessionService.calculatePayment();
    }

    @PutMapping("/parking-session/pay")
    public boolean payParkingSession(@RequestParam(required = false) String expireDate){
        return this.parkingSessionService.payParkingSession(expireDate);
    }

    @DeleteMapping({"/parking-session/{parkingSessionId}"})
    public void deleteParkingSession(@PathVariable int parkingSessionId) {
        this.parkingSessionService.deleteParkingSession(parkingSessionId);
    }
}
