package com.example.parkup.service;

import com.example.parkup.data.entities.Administrator;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface AdministratorService extends UserDetailsService {
    List<Administrator> getAllAdministrators();
    Administrator findById(int id);
    Administrator create(Administrator administrator);
    Administrator updateAdministrator(int id, Administrator administrator);
    void deleteAdministrator(int id);
}
