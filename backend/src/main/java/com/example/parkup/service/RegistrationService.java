package com.example.parkup.service;

import com.example.parkup.data.dto.RegistrationRequestDTO;

public interface RegistrationService {
    String register(RegistrationRequestDTO request);
    String confirmToken(String token);
    String buildEmail(String name, String link);

}
