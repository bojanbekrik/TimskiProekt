package com.example.parkup.repository;

import com.example.parkup.data.entities.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Integer> {
    @Query(nativeQuery = true, value = "SELECT * FROM guest g WHERE g.id = :id")
    Guest findById(@Param("id") int id);

    @Query(nativeQuery = true, value = "SELECT * FROM guest g WHERE g.email = :email")
    Guest findGuestByEmail(@Param("email") String email);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM guest WHERE id = :id")
    void deleteById(@Param("id") int id);
}
