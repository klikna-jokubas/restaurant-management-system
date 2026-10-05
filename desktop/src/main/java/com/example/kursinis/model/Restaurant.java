package com.example.kursinis.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor(force = true)
@Entity
@Table(name = "restaurants")
public class Restaurant extends BasicUser {

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    protected List<Cuisine> menu;

    @OneToMany(mappedBy = "restaurant")
    protected List<FoodOrder> foodOrders;

    {
        this.userType = "restaurant";
    }

    @Override
    public String toString() {
        return name + " (" + address + ")";
    }

}
