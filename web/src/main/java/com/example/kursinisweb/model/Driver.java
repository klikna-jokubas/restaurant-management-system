package com.example.kursinisweb.model;

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
@AllArgsConstructor
@Entity
@Table(name = "drivers")

public class Driver extends BasicUser{

    public Driver() {
        super();
        this.userType = "driver";
    }
    protected String licence;

    protected LocalDate bDate;

    @Enumerated(EnumType.STRING)
    protected VehicleType vehicleType;

}
