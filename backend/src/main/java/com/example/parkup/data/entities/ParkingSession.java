package com.example.parkup.data.entities;

import com.example.parkup.data.enumeration.SessionStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name = "parking_session")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSession {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @DateTimeFormat(pattern = "yyyy-MM-dd-HH-mm-ss")
    @Column(name = "time_start")
    private LocalDateTime timeStart;

    @DateTimeFormat(pattern = "yyyy-MM-dd-HH-mm-ss")
    @Column(name = "time_end")
    private LocalDateTime timeEnd;

    @OneToOne
    private Plate plate;

    @Enumerated
    @Column(name="session_status")
    private SessionStatus status;

    @ManyToOne
    private ParkingSpace parkingSpace;

    @ManyToOne
    private ParkingZone parkingZone;
}
