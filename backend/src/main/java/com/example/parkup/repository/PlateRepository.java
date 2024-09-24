package com.example.parkup.repository;

import com.example.parkup.data.entities.Plate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PlateRepository extends JpaRepository<Plate,Integer> {
    @Query(nativeQuery = true, value = "SELECT * FROM plate p WHERE p.plate = :plate")
    Plate findByPlate(@Param("plate") String plate);
}
