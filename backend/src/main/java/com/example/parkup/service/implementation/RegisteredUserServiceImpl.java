package com.example.parkup.service.implementation;

import com.example.parkup.ParkUpApplication;
import com.example.parkup.config.email.EmailValidator;
import com.example.parkup.data.entities.ConfirmationToken;
import com.example.parkup.data.entities.Plate;
import com.example.parkup.data.entities.RegisteredUser;
import com.example.parkup.repository.PlateRepository;
import com.example.parkup.repository.RegisteredUserRepository;
import com.example.parkup.service.ConfirmationTokenService;
import com.example.parkup.service.RegisteredUserService;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RegisteredUserServiceImpl implements RegisteredUserService {

    private final RegisteredUserRepository registeredUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailValidator emailValidator;
    private final PlateRepository plateRepository;

    public RegisteredUserServiceImpl(RegisteredUserRepository registeredUserRepository,
                                     BCryptPasswordEncoder bCryptPasswordEncoder,
                                     ConfirmationTokenService confirmationTokenService,
                                     EmailValidator emailValidator,
                                     PlateRepository plateRepository) {
        this.registeredUserRepository = registeredUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.confirmationTokenService = confirmationTokenService;
        this.emailValidator = emailValidator;
        this.plateRepository = plateRepository;
    }

    @Override
    public List<RegisteredUser> getRegisteredUsers() {
        return this.registeredUserRepository.findAll();
    }

    @Override
    public RegisteredUser addRegisteredUser(RegisteredUser registeredUser) {
        RegisteredUser existingRegisteredUser = this.registeredUserRepository.findRegisteredUserByEmail(registeredUser.getEmail());
        if (existingRegisteredUser != null) {
            throw new IllegalStateException("Email already taken, try adding a registriranParkirac with a different valid email address");
        }

        if (registeredUser.getEmail() == null || registeredUser.getEmail().equals("") || !emailValidator.test(registeredUser.getEmail())) {
            throw new IllegalStateException("email not valid");
        }

        this.registeredUserRepository.save(registeredUser);
        return registeredUser;
    }

    @Override
    @Transactional
    public RegisteredUser updateRegisteredUser(int registeredUserId, RegisteredUser registeredUser) {
        RegisteredUser existingRegisteredUser = this.registeredUserRepository.findById(registeredUserId);
        if (existingRegisteredUser == null){
            throw new IllegalStateException("Registered user doesn't exist, therefore can't be updated");
        }

        if (registeredUser.getEmail() == null || registeredUser.getEmail().equals("") || !emailValidator.test(registeredUser.getEmail())) {
            throw new IllegalStateException("email not valid");
        }
        RegisteredUser existingRegisteredUserByEmail = this.registeredUserRepository.findRegisteredUserByEmail(registeredUser.getEmail());
        if (existingRegisteredUserByEmail != null && existingRegisteredUserByEmail.getId() != existingRegisteredUser.getId()){
            throw new IllegalStateException("email taken");
        }
        existingRegisteredUser.setEmail(registeredUser.getEmail());

        if (registeredUser.getPassword() != null && !registeredUser.getPassword().equals("")) {
            existingRegisteredUser.setPassword(bCryptPasswordEncoder.encode(registeredUser.getPassword()));
        }

        if (registeredUser.getFirstName() != null && !registeredUser.getFirstName().equals("")) {
            existingRegisteredUser.setFirstName(registeredUser.getFirstName());
        }

        if (registeredUser.getLastName() != null && !registeredUser.getLastName().equals("")) {
            existingRegisteredUser.setLastName(registeredUser.getLastName());
        }

        if (registeredUser.getMobile() != null && !registeredUser.getMobile().equals("")) {
            existingRegisteredUser.setMobile(registeredUser.getMobile());
        }

        this.registeredUserRepository.save(existingRegisteredUser);
        return existingRegisteredUser;
    }

    @Override
    @Transactional
    @Modifying
    public void deleteRegisteredUser(int id) {
        RegisteredUser registeredUser = this.registeredUserRepository.findById(id);
        if (registeredUser == null){
            throw new IllegalStateException("Registered user doesn't exist, therefore can't be deleted");
        }

        this.registeredUserRepository.deleteById(id);
    }

    @Override
    public RegisteredUser findById(int id) {
        RegisteredUser registeredUser = this.registeredUserRepository.findById(id);
        if (registeredUser == null){
            throw new IllegalStateException("Registered user doesn't exist");
        }

        return registeredUser;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        RegisteredUser registeredUser = this.registeredUserRepository.findRegisteredUserByEmail(email);
        if (registeredUser == null) {
            throw new UsernameNotFoundException("Email not found");
        }

        return registeredUser;
    }

    @Override
    public String signUpParkingAttendant(RegisteredUser registeredUser) {
        if (registeredUser.getEmail() == null || registeredUser.getEmail().isEmpty())
            throw new IllegalArgumentException("Empty email");

        if (!emailValidator.test(registeredUser.getEmail()))
            throw new IllegalArgumentException("Invalid email");

        RegisteredUser existingParkingAttendant = this.registeredUserRepository.findRegisteredUserByEmail(registeredUser.getEmail());
        if(existingParkingAttendant != null){
            throw new IllegalStateException("Email already taken");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(registeredUser.getPassword());

        registeredUser.setPassword(encodedPassword);

        this.registeredUserRepository.save(registeredUser);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationTokenRP = new ConfirmationToken();
        confirmationTokenRP.setToken(token);
        confirmationTokenRP.setCreatedAt(LocalDateTime.now());
        confirmationTokenRP.setExpiresAt(LocalDateTime.now().plusMinutes(15));
        confirmationTokenRP.setRegisteredUser(registeredUser);

        confirmationTokenService.saveConfirmationTokenRP(confirmationTokenRP);

        return token;
    }

    @Override
    public void enableParkingAttendant(String email) {
        this.registeredUserRepository.enableRegisteredUser(email);
    }

    @Override
    @Transactional
    public String addPlate(int registeredUserId, Plate plate) {
        RegisteredUser registeredUser = this.registeredUserRepository.findById(registeredUserId);
        if (registeredUser == null){
            throw new IllegalStateException("Registered user doesn't exist");
        }

        registeredUser.getPlates().add(plate);

        this.registeredUserRepository.save(registeredUser);

        return plate.getPlate();
    }

    @Override
    @Transactional
    public String deletePlate(int registeredUserId, String plate) {
        RegisteredUser registeredUser = this.registeredUserRepository.findById(registeredUserId);
        if (registeredUser == null){
            throw new IllegalStateException("Registered user doesn't exist");
        }

        Plate p = this.plateRepository.findByPlate(plate);
        registeredUser.getPlates().remove(p);

        this.registeredUserRepository.save(registeredUser);

        return plate;
    }

    @Override
    public List<String> getPlates() {
        Authentication role = ParkUpApplication.getToken();

        RegisteredUser registeredUser = this.registeredUserRepository.findRegisteredUserByEmail(role.getName());
        if(registeredUser == null){
            throw new IllegalStateException("Registered user doesn't exist");
        }

        return registeredUser.getPlates().stream()
                .map(Plate::getPlate)
                .collect(Collectors.toList());
    }

}
