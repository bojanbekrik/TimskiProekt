package com.example.parkup.repository;

import com.example.parkup.data.entities.ConfirmationToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken,Integer> {
    @Query(nativeQuery = true, value = "SELECT * FROM confirmation_token a WHERE a.token = :token")
    ConfirmationToken findByToken(@Param("token") String token);

    @Query(nativeQuery = true, value = "DELETE FROM confirmation_token WHERE registered_parking_attendant_id = :registeredUserId")
    void deleteByRegisteredUser(@Param("registeredUserId") int registeredUserId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE confirmation_token SET confirmed_at = :confirmedAt WHERE token = :token")
    void updateConfirmedAt(@Param("token") String token, @Param("confirmedAt") LocalDateTime confirmedAt);

}
