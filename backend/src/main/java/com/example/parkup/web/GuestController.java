package com.example.parkup.web;

import com.example.parkup.data.entities.Guest;
import com.example.parkup.service.GuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GuestController {

    private final GuestService guestService;
    @Autowired
    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    @GetMapping("/guest")
    public List<Guest> getAllGuest() {
        return this.guestService.getGuests();
    }

    @GetMapping("/guest/{guestId}")
    public Guest getGuest(@PathVariable int guestId) {

        Guest guest = this.guestService.findById(guestId);
        if (guest != null) {
            return guest;
        } else {
            throw new RuntimeException("Guest not found");
        }

    }

    @PostMapping("/guest")
    public Guest addGuest(@RequestBody Guest guest) {
        Guest guestToReturn = this.guestService.addGuest(guest);
        return guestToReturn;
    }

    @DeleteMapping("/guest/{guestId}")
    public void deleteGuest(@PathVariable int guestId) {
        this.guestService.deleteGuest(guestId);
    }

}

