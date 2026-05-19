package com.example.kursinisweb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "basic_users")
public class BasicUser extends User {

    protected String address;

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    protected List<FoodOrder> myOrders;

    @OneToMany(mappedBy = "commentOwner", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    protected List<Review> myReviews;

    @OneToMany(mappedBy = "feedbackUser", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    protected List<Review> feedback;

    public BasicUser(String login, String password, String name, String surname, String phoneNumber, String address) {
        super();
        setLogin(login);
        setPassword(password);
        setName(name);
        setSurname(surname);
        setPhoneNumber(phoneNumber);
        setAddress(address);
    }
    public BasicUser() {
        super();
        this.userType = "client";
    }


}
