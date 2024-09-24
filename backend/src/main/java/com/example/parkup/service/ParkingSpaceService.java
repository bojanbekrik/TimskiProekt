package com.example.parkup.service;

import com.example.parkup.data.entities.ParkingSpace;

import java.util.List;

public interface ParkingSpaceService {
    List<ParkingSpace> getAllParkingSpaces();
    ParkingSpace findById(int id);
    ParkingSpace addParkingSpace(ParkingSpace parkingSpace);
    ParkingSpace updateParkingSpace(int id, ParkingSpace parkingSpace);
    void deleteParkingSpace(int id);
    long getNumberOfTakenSpaces();

}
