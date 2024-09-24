package com.example.parkup.service;

import com.example.parkup.data.entities.ConfirmationToken;

public interface ConfirmationTokenService {
    void saveConfirmationTokenRP(ConfirmationToken token);
    ConfirmationToken getTokenRP(String token);
    void deleteByRegisteredUser(int registeredUserId);
    void setConfirmedAtRP(String token);

}
