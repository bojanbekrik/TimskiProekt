package com.example.parkup.service.implementation;

import com.example.parkup.data.entities.ConfirmationToken;
import com.example.parkup.repository.ConfirmationTokenRepository;
import com.example.parkup.service.ConfirmationTokenService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public ConfirmationTokenServiceImpl(ConfirmationTokenRepository confirmationTokenRepository) {
        this.confirmationTokenRepository = confirmationTokenRepository;
    }
    public void saveConfirmationTokenRP(ConfirmationToken token){
        this.confirmationTokenRepository.save(token);
    }

    public ConfirmationToken getTokenRP(String token) {
        return this.confirmationTokenRepository.findByToken(token);
    }

    public void deleteByRegisteredUser(int registeredUserId){
        this.confirmationTokenRepository.deleteByRegisteredUser(registeredUserId);
    }

    public void setConfirmedAtRP(String token) {
        this.confirmationTokenRepository.updateConfirmedAt(token, LocalDateTime.now());
    }

}
