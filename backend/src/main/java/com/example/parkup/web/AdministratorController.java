package com.example.parkup.web;

import com.example.parkup.data.dto.RegistrationRequestDTO;
import com.example.parkup.data.entities.Administrator;
import com.example.parkup.service.AdministratorService;
import com.example.parkup.service.RegistrationService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AdministratorController {
    private final AdministratorService administratorService;
    private final RegistrationService registrationService;

    public AdministratorController(AdministratorService administratorService, RegistrationService registrationServiceRP) {
        this.administratorService = administratorService;
        this.registrationService = registrationServiceRP;
    }

    @GetMapping({"/administrators"})
    public List<Administrator> getAllAdministrator() {
        return this.administratorService.getAllAdministrators();
    }

    @GetMapping({"/administrators/{administratorId}"})
    public Administrator getAdministrator(@PathVariable int administratorId) {
        Administrator administrator = this.administratorService.findById(administratorId);
        if (administrator == null) {
            throw new RuntimeException("Administrator not found");
        }
        return administrator;
    }

    @PostMapping({"/administrator"})
    public Administrator addAdministrator(@RequestBody Administrator administrator) {
        return this.administratorService.create(administrator);
    }

    @PutMapping({"/administrator/{administratorId}"})
    public Administrator updateAdministrator(@PathVariable int administratorId, @RequestBody Administrator administrator) {
        return this.administratorService.updateAdministrator(administratorId, administrator);
    }

    @DeleteMapping({"/administrator/{administratorId}"})
    public void deleteAdministrator(@PathVariable int administratorId) {
        this.administratorService.deleteAdministrator(administratorId);
    }

    @PostMapping({"/administrator/registration"})
    public String register(@RequestBody RegistrationRequestDTO request){
        return registrationService.register(request);
    }

    @GetMapping(path = "/administrator/registration/confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }
}
