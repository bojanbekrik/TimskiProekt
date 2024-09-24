package com.example.parkup.service;

import com.example.parkup.data.dto.AddUpdateWorkerDTO;
import com.example.parkup.data.dto.WorkerDemoDTO;
import com.example.parkup.data.dto.WorkerDemoParkingZonesDTO;
import com.example.parkup.data.entities.Worker;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface WorkerService extends UserDetailsService {
    List<Worker> getWorkers();
    WorkerDemoParkingZonesDTO findById(int workerId);
    List<WorkerDemoDTO> getAllEmployeesDemos();
    Worker addWorker(AddUpdateWorkerDTO addUpdateWorkerDTO);
    WorkerDemoParkingZonesDTO updateWorker(int id, AddUpdateWorkerDTO addUpdateWorkerDTO);
    void deleteWorker(int id);
    void lockWorkerAcc(int id);
}
