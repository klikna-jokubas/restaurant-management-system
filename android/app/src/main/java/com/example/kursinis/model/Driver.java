package com.example.kursinis.model;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Vector;


public class Driver extends BasicUser{

    protected String licence;

    protected String bDate;

    private VehicleType vehicleType;

    public Driver() {
    }

    public Driver(VehicleType vehicleType, String bDate, String licence) {
        this.vehicleType = vehicleType;
        this.bDate = bDate;
        this.licence = licence;
    }

    public Driver(Integer id, String login, String password, String surname, String name, String phoneNumber, LocalDateTime dateCreated, LocalDateTime dateUpdated, Boolean isAdmin, String address, String licence, String bDate, VehicleType vehicleType) {
        super(id, login, password, surname, name, phoneNumber, dateCreated, dateUpdated, isAdmin, address);
        this.licence = licence;
        this.bDate = bDate;
        this.vehicleType = vehicleType;
    }

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public String getbDate() {
        return bDate;
    }

    public void setbDate(String bDate) {
        this.bDate = bDate;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }
}
