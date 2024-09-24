package com.example.parkup.data.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "parking_space")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSpace {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "ps_name")
    private String psName;

    @Column(name = "isTaken")
    private boolean taken;

    @Column(name = "latitude")
    private float lat;

    @Column(name = "longitude")
    private float lng;

    @Override
    public String toString() {
        return "ParkingSpace{" +
                "psName='" + psName + '\'' +
                ", isTaken=" + taken +
                ", lat=" + lat +
                ", lng=" + lng+
                '}';
    }
}
