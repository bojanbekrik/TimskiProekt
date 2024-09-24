package com.example.parkup.data.dto;

import com.example.parkup.data.entities.ParkingSpace;
import com.example.parkup.data.entities.ParkingZone;
import com.example.parkup.data.entities.ParkingZoneLocation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParkingZoneAdminDTO {
    private int pzId;
    private String pzName;
    private int price;
    private int from;
    private int to;
    private String address;
    private String color;
    private List<ParkingSpace> parkingSpaces;
    private ParkingZoneLocation parkingZoneLocation;
    private List<WorkerDemoDTO> responsibleWorkers;

    public static ParkingZoneAdminDTO mapToParkingZoneAdminDTO(ParkingZone parkingZone, List<WorkerDemoDTO> responsibleWorkers) {
        ParkingZoneAdminDTO parkingZoneAdminDTO = new ParkingZoneAdminDTO();

        parkingZoneAdminDTO.setPzId(parkingZone.getId());
        parkingZoneAdminDTO.setPzName(parkingZone.getPzName());
        parkingZoneAdminDTO.setPrice(parkingZone.getPrice());
        parkingZoneAdminDTO.setFrom(parkingZone.getFrom());
        parkingZoneAdminDTO.setTo(parkingZone.getTo());
        parkingZoneAdminDTO.setAddress(parkingZone.getAddress());
        parkingZoneAdminDTO.setColor(parkingZone.getColor());
        parkingZoneAdminDTO.setParkingSpaces(parkingZone.getParkingSpaces());
        parkingZoneAdminDTO.setParkingZoneLocation(parkingZone.getParkingZoneLocation());
        parkingZoneAdminDTO.setResponsibleWorkers(responsibleWorkers);

        return parkingZoneAdminDTO;
    }
}
