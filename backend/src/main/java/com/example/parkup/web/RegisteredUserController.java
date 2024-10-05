package com.example.parkup.web;

import com.example.parkup.data.dto.RegistrationRequestDTO;
import com.example.parkup.data.entities.Plate;
import com.example.parkup.data.entities.RegisteredUser;
import com.example.parkup.data.enumeration.SessionStatus;
import com.example.parkup.service.ParkingSessionService;
import com.example.parkup.service.RegisteredUserService;
import com.example.parkup.service.RegistrationService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class RegisteredUserController {
    private final RegisteredUserService registeredUserService;
    private final RegistrationService registrationService;
    private final ParkingSessionService parkingSessionService;

    public RegisteredUserController(RegisteredUserService registeredUserService, RegistrationService registrationServiceRP, ParkingSessionService parkingSessionService) {
        this.registeredUserService = registeredUserService;
        this.registrationService = registrationServiceRP;
        this.parkingSessionService = parkingSessionService;
    }

    @GetMapping({"/registered-users"})
    public List<RegisteredUser> getAllRegisteredUsers() {
        return this.registeredUserService.getRegisteredUsers();
    }

    @GetMapping({"/registered-user/{registeredUserId}"})
    public RegisteredUser getRegisteredUser(@PathVariable int registeredUserId) {
        RegisteredUser regPark = this.registeredUserService.findById(registeredUserId);
        if (regPark == null) {
            throw new RuntimeException("User not found");
        }
        return regPark;
    }

    @GetMapping({"/registered-user/session"})
    public SessionStatus getStatusOfParkingAttendant(){
        return parkingSessionService.getStatusOfParkingAttendant();
    }

    @PostMapping({"/registered-user"})
    public RegisteredUser addRegisteredUser(@RequestBody RegisteredUser registeredUser) {
        return this.registeredUserService.addRegisteredUser(registeredUser);
    }

    @PutMapping({"/registered-user/{registeredUserId}"})
    public RegisteredUser updateRegisteredUser(@PathVariable int registeredUserId, @RequestBody RegisteredUser registeredUser) {
        return this.registeredUserService.updateRegisteredUser(registeredUserId, registeredUser);
    }

    @PutMapping({"/registered-user/{registeredUserId}/plate"})
    public String addPlateToRegisteredUser(@PathVariable int registeredUserId, @RequestBody Plate plate){
        return this.registeredUserService.addPlate(registeredUserId, plate);
    }

    @GetMapping({"/registered-user/plates"})
    public List<String> getPlatesFromRegisteredUser(){
        return this.registeredUserService.getPlates();
    }

    @DeleteMapping({"/registered-user/{registeredUserId}/plates/{plate}"})
    public String deletePlateFromRegisteredUser(@PathVariable int registeredUserId, @PathVariable String plate){
        return this.registeredUserService.deletePlate(registeredUserId, plate);
    }

    @DeleteMapping({"/registered-user/{registeredUserId}"})
    public void deleteRegisteredUser(@PathVariable int registeredUserId) {
        this.registeredUserService.deleteRegisteredUser(registeredUserId);
    }

    @PostMapping({"/registered-user/registration"})
    public String register(@RequestBody RegistrationRequestDTO request){
        return registrationService.register(request);
    }

    @GetMapping(path = "/registered-user/registration/confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }

    @GetMapping({"/test-token"})
    public boolean testToken(){
        return true;
    }
}
