package com.example.parkup.data.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "parking_zone")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParkingZone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "pz_name")
    private String pzName;

    @Column(name = "price")
    private int price;

    @Transient
    @Column(name = "capacity")
    private int capacity;

    // working hours from
    @Column(name = "time_from")
    private int from;

    // working hours to
    @Column(name = "time_to")
    private int to;

    @Column(name = "address")
    private String address;

    @Transient
    @Column(name = "taken_spaces")
    private int takenSpaces;

    @Column(name = "color")
    private String color;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<ParkingSpace> parkingSpaces;

    @Transient
    private List<String> responsibleWorkers;

    @OneToOne(cascade = {CascadeType.ALL})
    private ParkingZoneLocation parkingZoneLocation;
}
