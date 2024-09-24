package com.example.parkup.data.dto;

import com.example.parkup.data.enumeration.EmployeeStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddUpdateWorkerDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String confirmPass;
    private List<String> parkingZones;
    private EmployeeStatus status;
    private String mobile;
    private boolean locked;
}
