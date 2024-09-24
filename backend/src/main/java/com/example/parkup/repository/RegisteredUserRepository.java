package com.example.parkup.repository;

import com.example.parkup.data.entities.RegisteredUser;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisteredUserRepository extends JpaRepository<RegisteredUser, Integer> {
    @Query(nativeQuery = true, value = "SELECT * FROM registered_user ru WHERE ru.id = :id")
    RegisteredUser findById(@Param("id") int id);

    @Query(nativeQuery = true, value = "SELECT * FROM registered_user ru WHERE ru.email = :email")
    RegisteredUser findRegisteredUserByEmail(@Param("email") String email);

    @Query(nativeQuery = true, value = "DELETE FROM registered_user WHERE id = :id")
    void deleteById(@Param("id") int id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE registered_user SET enabled = TRUE WHERE email = :email")
    void enableRegisteredUser(@Param("email") String email);
}
