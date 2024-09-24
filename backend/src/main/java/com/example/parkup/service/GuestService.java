package com.example.parkup.service;

import com.example.parkup.data.entities.Guest;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface GuestService extends UserDetailsService {
    List<Guest> getGuests();
    Guest findById(int guestId);
    Guest addGuest(Guest guest);
    void deleteGuest(int guestId);
}
