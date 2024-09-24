package com.example.parkup.repository;

import com.example.parkup.data.entities.ParkingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingSessionRepository extends JpaRepository<ParkingSession, Integer> {
    @Query(nativeQuery = true, value = "SELECT * FROM parking_session ps WHERE ps.id = :id")
    ParkingSession findById(@Param("id") int id);

    @Query(nativeQuery = true, value = "DELETE FROM parking_session WHERE id = :id")
    void deleteById(@Param("id") int id);
}
