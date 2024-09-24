package com.example.parkup.service;

import com.example.parkup.data.entities.Plate;
import com.example.parkup.data.entities.RegisteredUser;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface RegisteredUserService extends UserDetailsService {
    List<RegisteredUser> getRegisteredUsers();
    RegisteredUser addRegisteredUser(RegisteredUser registeredUser);
    RegisteredUser updateRegisteredUser(int id, RegisteredUser registeredUser);
    void deleteRegisteredUser(int id);
    RegisteredUser findById(int id);
    String signUpParkingAttendant(RegisteredUser registeredUser);
    void enableParkingAttendant(String email);
    String addPlate(int registeredUserId, Plate plate);
    String deletePlate(int registeredUserId, String plate);
    List<String> getPlates();
}
