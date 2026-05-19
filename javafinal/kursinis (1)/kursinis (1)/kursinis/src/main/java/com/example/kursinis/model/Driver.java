package com.example.kursinis.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Entity
@Table(name = "drivers")

public class Driver extends BasicUser{

    protected String licence;

    protected LocalDate bDate;

    @Enumerated(EnumType.STRING)
    protected VehicleType vehicleType;

    {
        this.userType = "driver";
    }

}
