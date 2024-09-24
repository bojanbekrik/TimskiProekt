package com.example.parkup.service.implementation;

import com.example.parkup.data.entities.ParkingSession;
import com.example.parkup.data.entities.ParkingSpace;
import com.example.parkup.data.entities.ParkingZone;
import com.example.parkup.repository.ParkingSessionRepository;
import com.example.parkup.repository.ParkingSpaceRepository;
import com.example.parkup.repository.ParkingZoneRepository;
import com.example.parkup.service.ParkingSpaceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ParkingSpaceServiceImpl implements ParkingSpaceService {

    private final ParkingSpaceRepository parkingSpaceRepository;
    private final ParkingZoneRepository parkingZoneRepository;
    private final ParkingSessionRepository parkingSessionRepository;

    public ParkingSpaceServiceImpl(ParkingSpaceRepository parkingSpaceRepository, ParkingZoneRepository parkingZoneRepository, ParkingSessionRepository parkingSessionRepository) {
        this.parkingSpaceRepository = parkingSpaceRepository;
        this.parkingZoneRepository = parkingZoneRepository;
        this.parkingSessionRepository = parkingSessionRepository;
    }

    @Override
    public List<ParkingSpace> getAllParkingSpaces() {
        return this.parkingSpaceRepository.findAll();
    }

    @Override
    public ParkingSpace findById(int id) {
        return this.parkingSpaceRepository.findById(id);
    }

    @Override
    public ParkingSpace addParkingSpace(ParkingSpace parkingSpace) {
        this.parkingSpaceRepository.save(parkingSpace);
        return parkingSpace;
    }

    @Override
    @Transactional
    public ParkingSpace updateParkingSpace(int parkingSpaceId, ParkingSpace parkingSpace) {
        ParkingSpace existingParkingSpace = this.parkingSpaceRepository.findById(parkingSpaceId);
        if (existingParkingSpace == null) {
            throw new IllegalStateException("Parking space does not exist");
        }

        if (parkingSpace.getPsName() != null && !parkingSpace.getPsName().equals("")) {
            ParkingSpace existingParkingSpaceByName = this.parkingSpaceRepository.findByPsName(parkingSpace.getPsName());
            if (existingParkingSpaceByName != null) {
                throw new IllegalStateException("There is already a Parking space with the same name");
            }
            existingParkingSpace.setPsName(parkingSpace.getPsName());
        }

        existingParkingSpace.setTaken(parkingSpace.isTaken());

        if (parkingSpace.getLat() != 0) {
            existingParkingSpace.setLat(parkingSpace.getLat());
        }

        if (parkingSpace.getLng() != 0) {
            existingParkingSpace.setLng(parkingSpace.getLng());
        }

        this.parkingSpaceRepository.save(existingParkingSpace);

        return existingParkingSpace;
    }

    @Override
    @Transactional
    public void deleteParkingSpace(int id) {
        ParkingSpace existingParkingSpace = this.parkingSpaceRepository.findById(id);
        if (existingParkingSpace == null) {
            throw new IllegalStateException("Parking space does not exist");
        }

        for (ParkingSession ps: this.parkingSessionRepository.findAll()){
            if (ps.getParkingSpace().getId() == existingParkingSpace.getId()){
                ps.setParkingSpace(null);
            }
        }

        for(ParkingZone pz : this.parkingZoneRepository.findAll()){
            pz.getParkingSpaces().remove(existingParkingSpace);
            this.parkingZoneRepository.save(pz);
        }

        this.parkingSpaceRepository.deleteByPsId(id);
    }

    @Override
    public long getNumberOfTakenSpaces() {
        return this.parkingSpaceRepository.findAll().stream().filter(ParkingSpace::isTaken).count();
    }
}
