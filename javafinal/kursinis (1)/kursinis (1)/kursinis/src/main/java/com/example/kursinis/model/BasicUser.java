package com.example.kursinis.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Entity
@Table(name = "basic_users")
public class BasicUser extends User {

    protected String address;

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL, orphanRemoval = true)
    protected List<FoodOrder> myOrders;

    @OneToMany(mappedBy = "commentOwner", cascade = CascadeType.ALL, orphanRemoval = true)
    protected List<Review> myReviews;

    @OneToMany(mappedBy = "feedbackUser", cascade = CascadeType.ALL, orphanRemoval = true)
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

    {
        this.userType = "driver";
    }

    @Override
    public String toString() {
        return name + " " + surname ;
    }

}
