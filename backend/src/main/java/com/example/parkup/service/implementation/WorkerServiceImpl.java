package com.example.parkup.service.implementation;

import com.example.parkup.config.email.EmailValidator;
import com.example.parkup.data.dto.AddUpdateWorkerDTO;
import com.example.parkup.data.dto.WorkerDemoDTO;
import com.example.parkup.data.dto.WorkerDemoParkingZonesDTO;
import com.example.parkup.data.entities.ParkingZone;
import com.example.parkup.data.entities.Worker;
import com.example.parkup.repository.ParkingZoneRepository;
import com.example.parkup.repository.WorkerRepository;
import com.example.parkup.service.WorkerService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class WorkerServiceImpl implements WorkerService {
    private final WorkerRepository workerRepository;
    private final ParkingZoneRepository parkingZoneRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailValidator emailValidator;

    public WorkerServiceImpl(WorkerRepository workerRepository, ParkingZoneRepository parkingZoneRepository, BCryptPasswordEncoder bCryptPasswordEncoder, EmailValidator emailValidator) {
        this.workerRepository = workerRepository;
        this.parkingZoneRepository = parkingZoneRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.emailValidator = emailValidator;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Worker worker = this.workerRepository.findWorkerByEmail(username);
        if (worker == null) {
            throw new IllegalStateException("Email not found");
        }

        return worker;
    }

    @Override
    public List<Worker> getWorkers() {
        return this.workerRepository.findAll();
    }

    @Override
    public WorkerDemoParkingZonesDTO findById(int workerId) {
        Worker worker = this.workerRepository.findById(workerId);
        if (worker == null) {
            throw new IllegalStateException("Worker doesn't exist");
        }

        return WorkerDemoParkingZonesDTO.mapToWorkerDemoParkingZonesDTO(worker);
    }

    @Override
    public List<WorkerDemoDTO> getAllEmployeesDemos() {
        List<Worker> workers = this.workerRepository.findAll();
        List<WorkerDemoDTO> workerDemos = new ArrayList<>();

        for (Worker w : workers){
            WorkerDemoDTO vd = WorkerDemoDTO.mapToWorkerDemoDTO(w);
            workerDemos.add(vd);
        }

        return workerDemos;
    }

    @Override
    public Worker addWorker(AddUpdateWorkerDTO addUpdateWorkerDTO) {
        Worker workerByEmail = this.workerRepository.findWorkerByEmail(addUpdateWorkerDTO.getEmail());
        if (workerByEmail != null){
            throw new IllegalArgumentException("User with that mail already exists");
        }

        Worker workerToAdd = new Worker();
        if (addUpdateWorkerDTO.getEmail() == null || addUpdateWorkerDTO.getEmail().equals("") || !emailValidator.test(addUpdateWorkerDTO.getEmail())) {
            throw new IllegalStateException("email not valid");
        }
        workerToAdd.setEmail(addUpdateWorkerDTO.getEmail());

        if(!addUpdateWorkerDTO.getPassword().isEmpty() && addUpdateWorkerDTO.getPassword().equals(addUpdateWorkerDTO.getConfirmPass())){
            workerToAdd.setPassword(bCryptPasswordEncoder.encode(addUpdateWorkerDTO.getPassword()));
        }

        workerToAdd.setLocked(addUpdateWorkerDTO.isLocked());

        if (addUpdateWorkerDTO.getFirstName() != null && !addUpdateWorkerDTO.getFirstName().equals("")) {
            workerToAdd.setFirstName(addUpdateWorkerDTO.getFirstName());
        }

        if (addUpdateWorkerDTO.getLastName() != null && !addUpdateWorkerDTO.getLastName().equals("")) {
            workerToAdd.setLastName(addUpdateWorkerDTO.getLastName());
        }

        if (addUpdateWorkerDTO.getMobile() != null && !addUpdateWorkerDTO.getMobile().equals("")) {
            workerToAdd.setMobile(addUpdateWorkerDTO.getMobile());
        }

        workerToAdd.setStatus(addUpdateWorkerDTO.getStatus());

        if(addUpdateWorkerDTO.getParkingZones() !=null ){
            List<ParkingZone> parkingZonesAvailable = parkingZoneRepository.findAll();
            List<ParkingZone> zonesToAdd = new ArrayList<>();
            workerToAdd.setParkingZones(null);

            for(String pzName : addUpdateWorkerDTO.getParkingZones()){
                for(ParkingZone pz : parkingZonesAvailable){
                    if(pzName.equals(pz.getPzName())) {
                        zonesToAdd.add(pz);
                    }
                }
            }
            workerToAdd.setParkingZones(zonesToAdd);
        }
        workerRepository.save(workerToAdd);

        return workerToAdd;
    }

    @Override
    @Transactional
    public WorkerDemoParkingZonesDTO updateWorker(int id, AddUpdateWorkerDTO addUpdateWorkerDTO) {
        Worker existingWorker = this.workerRepository.findById(id);
        if (existingWorker == null) {
            throw new IllegalStateException("Worker doesn't exist, therefore can't be updated");
        }

        if (addUpdateWorkerDTO.getEmail() == null || addUpdateWorkerDTO.getEmail().equals("") || !emailValidator.test(addUpdateWorkerDTO.getEmail())) {
            throw new IllegalStateException("email not valid");
        }
        Worker existingWorkerByEmail = this.workerRepository.findWorkerByEmail(addUpdateWorkerDTO.getEmail());
        if (existingWorkerByEmail != null){
            throw new IllegalStateException("email taken");
        }
        existingWorker.setEmail(addUpdateWorkerDTO.getEmail());

        if(!addUpdateWorkerDTO.getPassword().isEmpty() && addUpdateWorkerDTO.getPassword().equals(addUpdateWorkerDTO.getConfirmPass())){
            existingWorker.setPassword(bCryptPasswordEncoder.encode(addUpdateWorkerDTO.getPassword()));
        }

        existingWorker.setLocked(addUpdateWorkerDTO.isLocked());

        if (addUpdateWorkerDTO.getFirstName() != null && !addUpdateWorkerDTO.getFirstName().equals("")) {
            existingWorker.setFirstName(addUpdateWorkerDTO.getFirstName());
        }

        if (addUpdateWorkerDTO.getLastName() != null && !addUpdateWorkerDTO.getLastName().equals("")) {
            existingWorker.setLastName(addUpdateWorkerDTO.getLastName());
        }

        if (addUpdateWorkerDTO.getMobile() != null && !addUpdateWorkerDTO.getMobile().equals("")) {
            existingWorker.setMobile(addUpdateWorkerDTO.getMobile());
        }

        existingWorker.setStatus(addUpdateWorkerDTO.getStatus());

        if(addUpdateWorkerDTO.getParkingZones() !=null ){
            List<ParkingZone> parkingZonesAvailable = parkingZoneRepository.findAll();
            List<ParkingZone> zonesToAdd = new ArrayList<>();
            existingWorker.setParkingZones(null);

            for(String pzName : addUpdateWorkerDTO.getParkingZones()){
                for(ParkingZone pz : parkingZonesAvailable){
                    if(pzName.equals(pz.getPzName())) {
                        zonesToAdd.add(pz);
                    }
                }
            }
            existingWorker.setParkingZones(zonesToAdd);
        }

        this.workerRepository.save(existingWorker);

        return WorkerDemoParkingZonesDTO.mapToWorkerDemoParkingZonesDTO(existingWorker);
    }

    @Override
    public void deleteWorker(int id) {
        Worker worker = this.workerRepository.findById(id);
        if (worker == null) {
            throw new IllegalStateException("Worker doesn't exist, therefore can't be deleted");
        }

        this.workerRepository.deleteById(id);
    }

    @Override
    public void lockWorkerAcc(int id) {
        Worker worker = this.workerRepository.findById(id);
        if (worker == null) {
            throw new IllegalStateException("Worker doesn't exist, therefore can't be deleted");
        }

        worker.lockEmployee();

        this.workerRepository.save(worker);
    }
}
