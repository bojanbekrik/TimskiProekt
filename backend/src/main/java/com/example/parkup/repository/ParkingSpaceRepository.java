package com.example.parkup.repository;

import com.example.parkup.data.entities.ParkingSpace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingSpaceRepository extends JpaRepository<ParkingSpace, Integer> {
    @Query(nativeQuery = true, value = "SELECT * FROM parking_space ps WHERE ps.id = :id")
    ParkingSpace findById(@Param("id") int id);

    @Query(nativeQuery = true, value = "SELECT * FROM parking_space ps WHERE ps.ps_name = :psName")
    ParkingSpace findByPsName(@Param("psName") String psName);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM parking_space WHERE id = :id")
    void deleteByPsId(@Param("id") int id);
}
