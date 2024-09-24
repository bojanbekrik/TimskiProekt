package com.example.parkup.service.implementation;

import com.example.parkup.ParkUpApplication;
import com.example.parkup.data.entities.Guest;
import com.example.parkup.data.entities.ParkingSession;
import com.example.parkup.data.entities.ParkingSpace;
import com.example.parkup.data.entities.ParkingZone;
import com.example.parkup.data.entities.Plate;
import com.example.parkup.data.entities.RegisteredUser;
import com.example.parkup.data.enumeration.SessionStatus;
import com.example.parkup.repository.GuestRepository;
import com.example.parkup.repository.ParkingSessionRepository;
import com.example.parkup.repository.ParkingSpaceRepository;
import com.example.parkup.repository.ParkingZoneRepository;
import com.example.parkup.repository.PlateRepository;
import com.example.parkup.repository.RegisteredUserRepository;
import com.example.parkup.service.ParkingSessionService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParkingSessionServiceImpl implements ParkingSessionService {
    private final ParkingSessionRepository parkingSessionRepository;
    private final ParkingZoneRepository parkingZoneRepository;
    private final ParkingSpaceRepository parkingSpaceRepository;
    private final RegisteredUserRepository registeredUserRepository;
    private final PlateRepository plateRepository;
    private final GuestRepository guestRepository;

    public ParkingSessionServiceImpl(ParkingSessionRepository parkingSessionRepository,
                                     ParkingZoneRepository parkingZoneRepository,
                                     ParkingSpaceRepository parkingSpaceRepository,
                                     RegisteredUserRepository registeredUserRepository,
                                     PlateRepository plateRepository,
                                     GuestRepository guestRepository) {
        this.parkingSessionRepository = parkingSessionRepository;
        this.parkingZoneRepository = parkingZoneRepository;
        this.parkingSpaceRepository = parkingSpaceRepository;
        this.registeredUserRepository = registeredUserRepository;
        this.plateRepository = plateRepository;
        this.guestRepository = guestRepository;
    }

    @Override
    public List<ParkingSession> getAllParkingSessionsFromZone(int parkingZoneId) {
        return this.parkingSessionRepository.findAll().stream()
                .filter(ps -> !ps.getStatus().equals(SessionStatus.ENDED_PAID) &&
                        ps.getParkingZone() != null &&
                        ps.getParkingZone().getId() == parkingZoneId)
                .collect(Collectors.toList());
    }

    @Override
    public ParkingSession findById(int id) {
        return this.parkingSessionRepository.findById(id);
    }

    @Override
    @Transactional
    public void deleteParkingSession(int id) {
        ParkingSession parkingSession = this.parkingSessionRepository.findById(id);
        if (parkingSession == null){
            throw new IllegalStateException("Parking session doesn't exist, therefore can't be deleted");
        }

        if (parkingSession.getStatus() != SessionStatus.STARTED_UNVERIFIED) {
            parkingSession.getParkingSpace().setTaken(false);
        }

        parkingSession.setPlate(null);

        this.parkingSessionRepository.save(parkingSession);

        List<RegisteredUser> registeredUsers = this.registeredUserRepository.findAll();
        registeredUsers.stream()
                .filter(u -> u.getSession().getId() == (parkingSession.getId()))
                .forEach(u -> u.setSession(null));
        this.registeredUserRepository.saveAll(registeredUsers);

        List<Guest> guests = this.guestRepository.findAll();
        guests.stream()
                .filter(u -> u.getSession().getId() == (parkingSession.getId()))
                .forEach(u -> u.setSession(null));
        this.guestRepository.saveAll(guests);

        this.parkingSessionRepository.deleteById(id);
    }

    @Override
    @Transactional
    public ParkingSession startParkingSession(String plateName, String parkingZoneName) {
        ParkingZone parkingZone = this.parkingZoneRepository.findByPzName(parkingZoneName);
        if (parkingZone == null){
            throw new IllegalStateException("Parking zone doesn't exist");
        }

        Authentication token = ParkUpApplication.getToken();
        Plate plate = this.plateRepository.findByPlate(plateName);
        if (plate == null){
            throw new IllegalStateException("Plate doesn't exist");
        }

        ParkingSession sessionToAdd = new ParkingSession();
        sessionToAdd.setPlate(plate);
        sessionToAdd.setParkingZone(parkingZone);
        this.parkingSessionRepository.save(sessionToAdd);

        String role = token.getAuthorities().stream().findFirst().get().getAuthority();
        String email = token.getName();
        if(role.equals("ROLE_REG_USER")){
            RegisteredUser user = this.registeredUserRepository.findRegisteredUserByEmail(email);
            if (user == null){
                throw new IllegalStateException("User with that email doesn't exist");
            }

            user.setSession(sessionToAdd);

            this.registeredUserRepository.save(user);
        }
        else{
            Guest user = this.guestRepository.findGuestByEmail(email);
            if (user == null){
                throw new IllegalStateException("User with that email doesn't exist");
            }

            user.setSession(sessionToAdd);

            this.guestRepository.save(user);
        }

        return sessionToAdd;
    }

    @Override
    public int calculatePayment() {
        ParkingSession session = getParkingSession();
        if(session == null){
            throw new IllegalStateException("No such session exists");
        }
        else if(session.getStatus()!= SessionStatus.ENDED_UNPAID){
            throw new IllegalStateException("Cannot calculate payment on a session that is not ended");
        }
        int price = session.getParkingZone().getPrice();
        return (int) (Math.ceil((Duration.between(session.getTimeStart(),session.getTimeEnd()).toMinutes()+1)/60.0)*price);
    }

    @Override
    @Transactional
    public ParkingSession verifyParkingSession(int id, String parkingSpaceName) {
        ParkingSession parkingSession = this.parkingSessionRepository.findById(id);
        if(parkingSession == null){
            throw new IllegalStateException("No such session exists");
        }

        if(parkingSession.getStatus() == SessionStatus.STARTED_VERIFIED){
            throw new IllegalStateException("The session you are trying to verify has already been verified");
        }

        ParkingZone parkingZone = parkingSession.getParkingZone();
        if (parkingZone == null){
            throw new IllegalStateException("Parking zone doesn't exist");
        }

        ParkingSpace parkingSpace = this.parkingSpaceRepository.findByPsName(parkingSpaceName);
        if (parkingSpace == null){
            throw new IllegalStateException("Parking space by that name doesn't exist");
        }

        if(!parkingZone.getParkingSpaces().contains(parkingSpace)){
            throw new IllegalStateException("Invalid parking space inserted. Type in a new parking space or let the admin know that they should add the parking space to this zone");
        }

        if(parkingSpace.isTaken()){
            throw new IllegalStateException("Invalid parking space inserted. Type in a new parking space, this space is already in a session");
        }

        parkingSession.setStatus(SessionStatus.STARTED_VERIFIED);

        ParkingZone zone = this.parkingZoneRepository.findById(parkingZone.getId());
        if (zone == null){
            throw new IllegalStateException("Parking zone doesn't exist");
        }

        ParkingSpace space = zone.getParkingSpaces().stream()
                .filter(ps -> ps.getPsName().equals(parkingSpaceName))
                .findFirst().get();
        parkingSpace.setTaken(true);
        this.parkingSpaceRepository.save(space);

        parkingSession.setParkingSpace(space);

        this.parkingSessionRepository.save(parkingSession);

        return parkingSession;
    }

    @Override
    @Transactional
    public ParkingSession endParkingSession() {
        ParkingSession parkingSession = getParkingSession();
        if(parkingSession == null){
            throw new IllegalStateException("No such session exists");
        }

        if(parkingSession.getStatus() == SessionStatus.ENDED_UNPAID){
            throw new IllegalStateException("The session you are trying to end has already been ended");
        }
        parkingSession.setStatus(SessionStatus.ENDED_UNPAID);
        parkingSession.setTimeEnd(LocalDateTime.now());

        this.parkingSessionRepository.save(parkingSession);
        return parkingSession;
    }

    @Override
    @Transactional
    public boolean payParkingSession(String date) {
        if(!date.isEmpty()){
            String expMonthString = date.split("/")[0];
            String expYearString = date.split("/")[1];

            if(expMonthString.startsWith("0"))
                expMonthString = expMonthString.substring(1);
            if(expYearString.startsWith("0"))
                expYearString = expYearString.substring(1);
            int expMonth = Integer.parseInt(expMonthString);
            int expYear = Integer.parseInt(expYearString);
            int month = LocalDateTime.now().getMonth().getValue();
            int year = LocalDateTime.now().getYear()%100;

            if(year>expYear){
                throw new IllegalStateException("Your card has expired or the date that you have entered is incorrect");
            }
            else if(year==expYear){
                if(month>=expMonth)
                    throw new IllegalStateException("Your card has expired or the date that you have entered is incorrect");
            }
        }

        ParkingSession parkingSession = getParkingSession();
        if(parkingSession == null){
            throw new IllegalStateException("No such session exists");
        }

        if(parkingSession.getStatus() == SessionStatus.ENDED_PAID){
            throw new IllegalStateException("The session has already been paid for");
        }

        parkingSession.setStatus(SessionStatus.ENDED_PAID);
        if(parkingSession.getParkingSpace() != null) {
            parkingSession.getParkingSpace().setTaken(false);
        }
        parkingSession.setPlate(null);

        List<RegisteredUser> registeredUsers = this.registeredUserRepository.findAll();
        registeredUsers.stream()
                .filter(ru -> ru.getSession() != null && ru.getSession().getStatus().equals(SessionStatus.ENDED_PAID))
                .forEach(ru -> ru.setSession(null));
        this.registeredUserRepository.saveAll(registeredUsers);

        List<Guest> guests = this.guestRepository.findAll();
        guests.stream()
                .filter(g -> g.getSession() !=null && g.getSession().getStatus().equals(SessionStatus.ENDED_PAID))
                .forEach(u -> u.setSession(null));
        this.guestRepository.saveAll(guests);
        this.guestRepository.deleteAll(this.guestRepository.findAll().stream().filter(g-> g.getSession() == null).collect(Collectors.toList()));

        parkingSessionRepository.deleteById(parkingSession.getId());

        return true;
    }

    @Override
    public SessionStatus getStatusOfParkingAttendant() {
        ParkingSession session = getParkingSession();

        return session == null ? null : session.getStatus();
    }

    @Override
    public ParkingSession getParkingSession() {
        Authentication token = ParkUpApplication.getToken();
        String role = token.getAuthorities().stream().findFirst().get().getAuthority();
        String email =token.getName();
        ParkingSession parkingSession;
        if(role.equals("ROLE_REG_USER")){
            RegisteredUser user = this.registeredUserRepository.findRegisteredUserByEmail(email);
            if (user == null){
                throw new IllegalStateException("User with that email doesn't exist");
            }

            parkingSession = user.getSession();
        }
        else{
            Guest user = this.guestRepository.findGuestByEmail(email);
            if (user == null){
                throw new IllegalStateException("User with that email doesn't exist");
            }

            parkingSession = user.getSession();
        }

        return parkingSession;
    }
}
