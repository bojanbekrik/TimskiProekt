package com.example.parkup.web;

import com.example.parkup.data.dto.AddUpdateWorkerDTO;
import com.example.parkup.data.dto.WorkerDemoDTO;
import com.example.parkup.data.dto.WorkerDemoParkingZonesDTO;
import com.example.parkup.data.entities.Worker;
import com.example.parkup.service.WorkerService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WorkerController {
    private final WorkerService workerService;

    public WorkerController(WorkerService workerService) {
        this.workerService = workerService;
    }

    @GetMapping({"/employees"})
    public List<Worker> getAllEmployees() {
        return this.workerService.getWorkers();
    }

    @GetMapping({"/employees/{employeeId}"})
    public WorkerDemoParkingZonesDTO getEmployee(@PathVariable int employeeId) {
        WorkerDemoParkingZonesDTO employee = this.workerService.findById(employeeId);
        if (employee == null) {
            throw new RuntimeException("Vraboten not found");
        }
        return employee;
    }

    @GetMapping({"/employees/employee-demos"})
    public List<WorkerDemoDTO> getEmployeesDemos(){
        return this.workerService.getAllEmployeesDemos();
    }

    @PostMapping({"/employee"})
    public Worker addEmployee(@RequestBody AddUpdateWorkerDTO employee) {
        return this.workerService.addWorker(employee);
    }

    @PutMapping({"/employee/lock/{employeeId}"})
    public void lockEmployee(@PathVariable int employeeId) {
        this.workerService.lockWorkerAcc(employeeId);
    }

    @PutMapping({"/employee/{employeeId}"})
    public WorkerDemoParkingZonesDTO updateEmployee(@PathVariable int employeeId, @RequestBody AddUpdateWorkerDTO employee) {
        return this.workerService.updateWorker(employeeId, employee);
    }

    @DeleteMapping({"/employee/{employeeId}"})
    public void deleteEmployee(@PathVariable int employeeId) {
        this.workerService.deleteWorker(employeeId);
    }
}
