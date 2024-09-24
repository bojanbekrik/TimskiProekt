package com.example.parkup.service.implementation;

import com.example.parkup.config.email.EmailValidator;
import com.example.parkup.data.entities.Administrator;
import com.example.parkup.repository.AdministratorRepository;
import com.example.parkup.service.AdministratorService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class AdministratorServiceImpl implements AdministratorService {
    private final AdministratorRepository administratorRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailValidator emailValidator;

    public AdministratorServiceImpl(AdministratorRepository administratorRepository, BCryptPasswordEncoder bCryptPasswordEncoder, EmailValidator emailValidator) {
        this.administratorRepository = administratorRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.emailValidator = emailValidator;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Administrator administrator = this.administratorRepository.findAdministratorByEmail(username);
        if (administrator == null) {
            throw new IllegalStateException("Email not found");
        }

        return administrator;
    }

    @Override
    public List<Administrator> getAllAdministrators() {
        return this.administratorRepository.findAll();
    }

    @Override
    public Administrator findById(int id) {
        return this.administratorRepository.findById(id);
    }

    @Override
    public Administrator create(Administrator administrator) {
        Administrator existingAdministrator = this.administratorRepository.findAdministratorByEmail(administrator.getEmail());
        if (existingAdministrator != null) {
            throw new IllegalStateException("Email already taken, try adding an administrator with a different valid email address");
        }

        if (administrator.getEmail() == null || Objects.equals(administrator.getEmail(), "") || !emailValidator.test(administrator.getEmail())) {
            throw new IllegalStateException("Email not valid");
        }

        administrator.setPassword(bCryptPasswordEncoder.encode(administrator.getPassword()));
        this.administratorRepository.save(administrator);

        return administrator;
    }

    @Override
    @Transactional
    public Administrator updateAdministrator(int id, Administrator administrator) {
        Administrator existingAdministrator = this.administratorRepository.findById(id);
        if (existingAdministrator == null) {
            throw new IllegalStateException("Administrator doesn't exist, therefore can't be updated");
        }

        if (administrator.getEmail() == null || Objects.equals(administrator.getEmail(), "") || !emailValidator.test(administrator.getEmail())) {
            throw new IllegalStateException("Email not valid");
        }

        Administrator existingEmailAdministrator = this.administratorRepository.findAdministratorByEmail(administrator.getEmail());
        if (existingEmailAdministrator != null) {
            throw new IllegalStateException("Email already taken");
        }

        administrator.setId(id);
        this.administratorRepository.save(administrator);
        return administrator;
    }

    @Override
    public void deleteAdministrator(int id) {
        this.administratorRepository.deleteById(id);
    }
}
