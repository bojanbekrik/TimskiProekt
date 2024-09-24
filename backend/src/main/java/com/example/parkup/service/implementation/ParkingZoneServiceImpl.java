package com.example.parkup.service.implementation;

import com.example.parkup.ParkUpApplication;
import com.example.parkup.data.dto.ParkingZoneAdminDTO;
import com.example.parkup.data.dto.WorkerDemoDTO;
import com.example.parkup.data.entities.ParkingSpace;
import com.example.parkup.data.entities.ParkingZone;
import com.example.parkup.data.entities.Worker;
import com.example.parkup.repository.ParkingSpaceRepository;
import com.example.parkup.repository.ParkingZoneRepository;
import com.example.parkup.repository.WorkerRepository;
import com.example.parkup.service.ParkingZoneService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ParkingZoneServiceImpl implements ParkingZoneService {
    private final ParkingZoneRepository parkingZoneRepository;
    private final ParkingSpaceRepository parkingSpaceRepository;
    private final WorkerRepository workerRepository;

    public ParkingZoneServiceImpl(ParkingZoneRepository parkingZoneRepository, ParkingSpaceRepository parkingSpaceRepository, WorkerRepository workerRepository) {
        this.parkingZoneRepository = parkingZoneRepository;
        this.parkingSpaceRepository = parkingSpaceRepository;
        this.workerRepository = workerRepository;
    }

    @Override
    public List<ParkingZone> getAllParkingZones() {
        Authentication user = ParkUpApplication.getToken();
        String role = user.getAuthorities().stream().findFirst().get().getAuthority();
        String email = user.getName();
        List<ParkingZone> parkingZones;
        if ("ROLE_WORKER".equals(role)) {
            Worker loggedInWorker = this.workerRepository.findWorkerByEmail(email);
            if (loggedInWorker == null) {
                throw new IllegalStateException("User with that email doesn't exist");
            }
            parkingZones = this.parkingZoneRepository.findAll().stream().filter(pz -> loggedInWorker.getParkingZones().contains(pz)).collect(Collectors.toList());
        } else {
            parkingZones = this.parkingZoneRepository.findAll();
        }
        for(ParkingZone pz:parkingZones){
            setTransientVariables(pz);
        }

        return parkingZones;
    }

    @Override
    public ParkingZoneAdminDTO findById(int parkingZoneId) {
        ParkingZone parkingZone = this.parkingZoneRepository.findById(parkingZoneId);
        if (parkingZone == null) {
            throw new IllegalStateException("ParkingZone does not exist");
        }
        setTransientVariables(parkingZone);

        return ParkingZoneAdminDTO.mapToParkingZoneAdminDTO(parkingZone, getResponsibleWorkers(parkingZoneId));
    }

    @Override
    public List<String> getAllParkingZoneNames() {
        List<ParkingZone> parkingZones = this.parkingZoneRepository.findAll();
        List<String> parkingZonesNames = new ArrayList<>();
        for (ParkingZone pz : parkingZones) {
            parkingZonesNames.add(pz.getPzName());
        }

        return parkingZonesNames;
    }

    @Override
    public ParkingZone addParkingZone(ParkingZone parkingZone) {
        ParkingZone zone = this.parkingZoneRepository.findByPzName(parkingZone.getPzName());
        if (zone != null) {
            throw new IllegalStateException("Name already taken, try adding a ParkingZone with a different name");
        }
        this.parkingZoneRepository.save(parkingZone);

        return parkingZone;
    }

    @Override
    public ParkingZone addParkingZoneNameOnly(String name) {
        ParkingZone zone = this.parkingZoneRepository.findByPzName(name);
        if (zone != null) {
            throw new IllegalStateException("Name already taken, try adding a ParkingZone with a different name");
        }
        ParkingZone parkingZone = new ParkingZone();
        parkingZone.setPzName(name);
        parkingZone.setTakenSpaces(0);
        parkingZone.setParkingSpaces(new ArrayList<>());
        this.parkingZoneRepository.save(parkingZone);

        return parkingZone;
    }

    @Override
    @Transactional
    public ParkingZoneAdminDTO updateParkingZone(int parkingZoneId, ParkingZoneAdminDTO parkingZoneAdminDTO) {
        ParkingZone oldParkingZone = this.parkingZoneRepository.findById(parkingZoneId);
        ParkingZone parkingZone = this.parkingZoneRepository.findById(parkingZoneId);
        if (parkingZone == null) {
            throw new IllegalStateException("The parking zone does not exist");
        }

        if (parkingZoneAdminDTO.getPzName() != null && !parkingZoneAdminDTO.getPzName().equals("")) {
            parkingZone.setPzName(parkingZoneAdminDTO.getPzName());
        }

        if (parkingZoneAdminDTO.getPrice() != 0) {
            parkingZone.setPrice(parkingZoneAdminDTO.getPrice());
        }

        if (parkingZoneAdminDTO.getAddress() != null && !parkingZoneAdminDTO.getAddress().equals("")) {
            parkingZone.setAddress(parkingZoneAdminDTO.getAddress());
        }

        if (parkingZoneAdminDTO.getFrom() != 0) {
            parkingZone.setFrom(parkingZoneAdminDTO.getFrom());
        }

        if (parkingZoneAdminDTO.getTo() != 0) {
            parkingZone.setTo(parkingZoneAdminDTO.getTo());
        }

        if (parkingZoneAdminDTO.getColor() != null && !parkingZoneAdminDTO.getColor().equals("")) {
            parkingZone.setColor(parkingZoneAdminDTO.getColor());
        }

        if (parkingZoneAdminDTO.getParkingZoneLocation() != null) {
            parkingZone.setParkingZoneLocation(parkingZoneAdminDTO.getParkingZoneLocation());
        }

        List<ParkingSpace> spacesToDelete = parkingZone.getParkingSpaces();
        this.parkingSpaceRepository.deleteAll(spacesToDelete);
        parkingZone.setParkingSpaces(new ArrayList<>());

        if (parkingZoneAdminDTO.getParkingSpaces() != null && !parkingZoneAdminDTO.getParkingSpaces().isEmpty()) {
            parkingZone.setParkingSpaces(parkingZoneAdminDTO.getParkingSpaces());
        }

        if (parkingZoneAdminDTO.getResponsibleWorkers() != null && !parkingZoneAdminDTO.getResponsibleWorkers().isEmpty()) {
            List<Integer> workerIds = getResponsibleWorkers(parkingZoneId).stream().map(WorkerDemoDTO::getWorkerId).toList();
            for(Integer workerId : workerIds){
                Optional<Worker> worker = this.workerRepository.findById(workerId);
                if (worker.isEmpty()) {
                    throw new IllegalStateException("The worker does not exist");
                }
                worker.get().getParkingZones().remove(oldParkingZone);
                this.workerRepository.save(worker.get());
            }

            List<Integer> responsibleWorkerIds = parkingZoneAdminDTO.getResponsibleWorkers().stream().map(WorkerDemoDTO::getWorkerId).toList();
            for (Integer responsibleWorkerId : responsibleWorkerIds) {
                Optional<Worker> worker = this.workerRepository.findById(responsibleWorkerId);
                if (worker.isEmpty()) {
                    throw new IllegalStateException("The worker does not exist");
                }
                worker.get().getParkingZones().add(oldParkingZone);
                this.workerRepository.save(worker.get());
            }
        } else {
            List<Worker> workers = this.workerRepository.findAll().stream().filter(w -> w.getParkingZones().contains(oldParkingZone)).toList();
            workers.forEach(w -> w.getParkingZones().remove(oldParkingZone));
            this.workerRepository.saveAll(workers);
        }

        setTransientVariables(parkingZone);

        return ParkingZoneAdminDTO.mapToParkingZoneAdminDTO(parkingZone, getResponsibleWorkers(parkingZoneId));
    }

    @Override
    public void deleteParkingZone(int id) {
        ParkingZone parkingZone = this.parkingZoneRepository.findById(id);
        if (parkingZone == null) {
            throw new IllegalStateException("Parking zone doesn't exist, therefore can't be deleted");
        }

        parkingZone.setParkingSpaces(null);
        parkingZone.setParkingZoneLocation(null);

        this.parkingZoneRepository.save(parkingZone);

        List<Worker> workers = this.workerRepository.findAll().stream()
                .filter(w -> w.getParkingZones().stream()
                        .map(ParkingZone::getId)
                        .anyMatch(pzId -> pzId.equals(parkingZone.getId())))
                .toList();
        workers.forEach(w -> w.getParkingZones().remove(parkingZone));
        this.workerRepository.saveAll(workers);
        this.parkingZoneRepository.deleteById(id);
    }

    @Override
    public int calculateTakenSpaces(int id) {
        ParkingZone parkingZone = this.parkingZoneRepository.findById(id);
        if (parkingZone == null) {
            throw new IllegalStateException("Parking zone doesn't exist");
        }

        return (int)parkingZone.getParkingSpaces().stream().filter(ParkingSpace::isTaken).count();
    }

    @Override
    public int calculateCapacity(int id) {
        ParkingZone parkingZone = this.parkingZoneRepository.findById(id);
        if (parkingZone == null) {
            throw new IllegalStateException("Parking zone doesn't exist");
        }

        return parkingZone.getParkingSpaces().size();
    }

    @Override
    public void setTransientVariables(ParkingZone parkingZone) {
        parkingZone.setCapacity(calculateCapacity(parkingZone.getId()));
        parkingZone.setTakenSpaces(calculateTakenSpaces(parkingZone.getId()));
        parkingZone.setResponsibleWorkers(getWorkers(parkingZone.getId()));
    }

    @Override
    public List<WorkerDemoDTO> getResponsibleWorkers(int parkingZoneId) {
        ParkingZone parkingZone = this.parkingZoneRepository.findById(parkingZoneId);
        if (parkingZone == null) {
            throw new IllegalStateException("Parking zone doesn't exist");
        }

        List<Worker> workers = this.workerRepository.findAll().stream()
                .filter(w -> w.getParkingZones().contains(parkingZone))
                .toList();

        return workers.stream().map(WorkerDemoDTO::mapToWorkerDemoDTO).toList();
    }

    @Override
    public List<String> getWorkers(int parkingZoneId) {
        ParkingZone parkingZone = this.parkingZoneRepository.findById(parkingZoneId);
        if (parkingZone == null) {
            throw new IllegalStateException("Parking zone doesn't exist");
        }

        return this.workerRepository.findAll().stream()
                .filter(w->w.getParkingZones().contains(parkingZone))
                .map(w->w.getFirstName() + " " + w.getLastName())
                .collect(Collectors.toList());
    }

    @Override
    public ParkingZone getParkingZoneByName(String parkingZoneName) {
        ParkingZone parkingZone = this.parkingZoneRepository.findByPzName(parkingZoneName);
        if (parkingZone == null) {
            throw new IllegalStateException("Parking zone doesn't exist");
        }
        setTransientVariables(parkingZone);
        return parkingZone;
    }
}
