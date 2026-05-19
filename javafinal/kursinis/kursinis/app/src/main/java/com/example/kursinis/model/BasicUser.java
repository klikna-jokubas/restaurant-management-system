package com.example.kursinis.model;


import java.time.LocalDateTime;

public class BasicUser extends User {



    public BasicUser(){
    }

    public BasicUser(Integer id, String login, String password, String surname, String name, String phoneNumber, LocalDateTime dateCreated, LocalDateTime dateUpdated, Boolean isAdmin, String address) {
        super(id, login, password, surname, name, phoneNumber, dateCreated, dateUpdated, isAdmin);
        this.address = address;
    }


    public String getAddress() {
        return address;
    }

}
