package com.example.parkup.service;

import com.example.parkup.data.entities.ParkingSession;
import com.example.parkup.data.enumeration.SessionStatus;

import java.util.List;

public interface ParkingSessionService {
    List<ParkingSession> getAllParkingSessionsFromZone(int parkingZoneId);
    ParkingSession findById(int id);
    void deleteParkingSession(int id);
    ParkingSession startParkingSession(String plateName, String parkingZoneName);
    int calculatePayment();
    ParkingSession verifyParkingSession(int id, String parkingSpaceName);
    ParkingSession endParkingSession();
    boolean payParkingSession(String date);
    SessionStatus getStatusOfParkingAttendant();
    ParkingSession getParkingSession();
}
