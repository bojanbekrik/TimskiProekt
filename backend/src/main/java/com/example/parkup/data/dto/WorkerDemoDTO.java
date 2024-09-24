package com.example.parkup.data.dto;

import com.example.parkup.data.entities.Worker;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkerDemoDTO {
    private int workerId;
    private String firstName;
    private String lastName;
    private String email;

    public static WorkerDemoDTO mapToWorkerDemoDTO(Worker worker) {
        WorkerDemoDTO workerDemoDTO = new WorkerDemoDTO();

        workerDemoDTO.setWorkerId(worker.getId());
        workerDemoDTO.setFirstName(worker.getFirstName());
        workerDemoDTO.setLastName(worker.getLastName());
        workerDemoDTO.setEmail(worker.getEmail());

        return workerDemoDTO;
    }
}
