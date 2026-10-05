package com.example.kursinis.model;


import java.time.LocalDateTime;


public class Restaurant extends BasicUser {

    public Restaurant(Integer id, String login, String password, String surname, String name, String phoneNumber, LocalDateTime dateCreated, LocalDateTime dateUpdated, Boolean isAdmin, String address) {
        super(id, login, password, surname, name, phoneNumber, dateCreated, dateUpdated, isAdmin, address);
    }

    @Override
    public String toString() {
        return name + " (" + address + ")";
    }
}
