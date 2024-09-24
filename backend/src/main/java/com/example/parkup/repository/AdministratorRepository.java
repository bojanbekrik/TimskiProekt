package com.example.parkup.repository;

import com.example.parkup.data.entities.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {
    @Query(nativeQuery = true, value = "SELECT * FROM administrator a WHERE a.id = :id")
    Administrator findById(@Param("id") int id);

    @Query(nativeQuery = true, value = "SELECT * FROM administrator a WHERE a.email = :email")
    Administrator findAdministratorByEmail(@Param("email") String email);

    @Query(nativeQuery = true, value = "DELETE FROM administrator WHERE id = :id")
    void deleteById(@Param("id") int id);
}
