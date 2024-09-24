package com.example.parkup.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String mobile;
}
