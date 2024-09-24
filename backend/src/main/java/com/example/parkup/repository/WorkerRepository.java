package com.example.parkup.repository;

import com.example.parkup.data.entities.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Integer> {
    @Query(nativeQuery = true, value = "SELECT * FROM worker w WHERE w.id = :id")
    Worker findById(@Param("id") int id);

    @Query(nativeQuery = true, value = "SELECT * FROM worker w WHERE w.email = :email")
    Worker findWorkerByEmail(@Param("email") String email);

    @Query(nativeQuery = true, value = "DELETE FROM worker WHERE id = :id")
    void deleteById(@Param("id") int id);
}
