package com.example.parkup.repository;

import com.example.parkup.data.entities.ParkingZone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingZoneRepository extends JpaRepository<ParkingZone, Integer> {
    @Query(nativeQuery = true, value = "SELECT * FROM parking_zone pz WHERE pz.id = :id")
    ParkingZone findById(@Param("id") int id);

    @Query(nativeQuery = true, value = "SELECT * FROM parking_zone pz WHERE pz.pz_name = :pzName")
    ParkingZone findByPzName(@Param("pzName") String name);

    @Query(nativeQuery = true, value = "DELETE FROM parking_zone WHERE id = :id")
    void deleteById(@Param("id") int id);
}
