package com.example.parkup.data.dto;

import com.example.parkup.data.entities.ParkingZone;
import com.example.parkup.data.entities.Worker;
import com.example.parkup.data.enumeration.EmployeeStatus;
import com.example.parkup.data.enumeration.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkerDemoParkingZonesDTO {
    private int workerId;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private UserRole role;
    private boolean locked;
    private EmployeeStatus status;
    private List<String> pzNames;

    public static WorkerDemoParkingZonesDTO mapToWorkerDemoParkingZonesDTO(Worker worker) {
        WorkerDemoParkingZonesDTO workerDemoParkingZonesDTO = new WorkerDemoParkingZonesDTO();

        workerDemoParkingZonesDTO.setWorkerId(worker.getId());
        workerDemoParkingZonesDTO.setFirstName(worker.getFirstName());
        workerDemoParkingZonesDTO.setLastName(worker.getLastName());
        workerDemoParkingZonesDTO.setEmail(worker.getEmail());
        workerDemoParkingZonesDTO.setMobile(worker.getMobile());
        workerDemoParkingZonesDTO.setRole(worker.getRole());
        workerDemoParkingZonesDTO.setStatus(worker.getStatus());
        workerDemoParkingZonesDTO.setLocked(!worker.isAccountNonLocked());
        workerDemoParkingZonesDTO.setPzNames(worker.getParkingZones().stream().map(ParkingZone::getPzName).collect(Collectors.toList()));

        return workerDemoParkingZonesDTO;
    }
}
