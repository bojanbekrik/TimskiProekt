package com.example.parkup.service;

import com.example.parkup.data.dto.ParkingZoneAdminDTO;
import com.example.parkup.data.dto.WorkerDemoDTO;
import com.example.parkup.data.entities.ParkingZone;

import java.util.List;

public interface ParkingZoneService {
    List<ParkingZone> getAllParkingZones();
    ParkingZoneAdminDTO findById(int parkingZoneId);
    List<String> getAllParkingZoneNames();
    ParkingZone addParkingZone(ParkingZone parkingZone);
    ParkingZone addParkingZoneNameOnly(String name);
    ParkingZoneAdminDTO updateParkingZone(int parkingZoneId, ParkingZoneAdminDTO parkingZoneAdminDTO);
    void deleteParkingZone (int id);
    int calculateTakenSpaces(int id);
    int calculateCapacity(int id);
    void setTransientVariables(ParkingZone parkingZone);
    List<WorkerDemoDTO> getResponsibleWorkers(int parkingZoneId);
    List<String> getWorkers(int parkingZoneId);
    ParkingZone getParkingZoneByName(String parkingZoneName);
}
