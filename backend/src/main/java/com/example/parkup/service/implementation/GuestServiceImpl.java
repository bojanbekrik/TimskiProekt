package com.example.parkup.service.implementation;

import com.example.parkup.data.entities.Guest;
import com.example.parkup.data.entities.ParkingSession;
import com.example.parkup.data.entities.Plate;
import com.example.parkup.repository.GuestRepository;
import com.example.parkup.repository.ParkingSessionRepository;
import com.example.parkup.repository.PlateRepository;
import com.example.parkup.service.GuestService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GuestServiceImpl implements GuestService {
    private final GuestRepository guestRepository;
    private final PlateRepository plateRepository;
    private final ParkingSessionRepository parkingSessionRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public GuestServiceImpl(GuestRepository guestRepository, PlateRepository plateRepository, ParkingSessionRepository parkingSessionRepository, BCryptPasswordEncoder passwordEncoder) {
        this.guestRepository = guestRepository;
        this.plateRepository = plateRepository;
        this.parkingSessionRepository = parkingSessionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<Guest> getGuests() {
        return this.guestRepository.findAll();
    }

    @Override
    public Guest findById(int guestId) {
        return this.guestRepository.findById(guestId);
    }

    @Override
    public Guest addGuest(Guest guest) {
        Guest existingGuest = this.guestRepository.findGuestByEmail(guest.getEmail());
        if (existingGuest != null) {
            throw new IllegalStateException("Email already taken, try adding a guest with a different valid email address");
        }

        Plate existingPlate = plateRepository.findById(guest.getPlate().getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid plate ID"));

        guest.setPlate(existingPlate);

        ParkingSession existingSession = parkingSessionRepository.findById(guest.getSession().getId());
        guest.setSession(existingSession);

        double random = Math.random()*10000;
        guest.setPassword(passwordEncoder.encode(Integer.toString((int)random)));
        this.guestRepository.save(guest);
        guest.setPassword(Integer.toString((int)random));

        return guest;
    }

    @Override
    @Transactional
    public void deleteGuest(int guestId) {
        Guest guest = this.guestRepository.findById(guestId);
        if (guest == null){
            throw new IllegalStateException("Guest doesn't exist, therefore can't be deleted");
        }

        this.guestRepository.deleteById(guestId);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Guest guest = this.guestRepository.findGuestByEmail(s);
        if (guest == null) {
            throw new UsernameNotFoundException("Email not found");
        }

        return guest;
    }

}
