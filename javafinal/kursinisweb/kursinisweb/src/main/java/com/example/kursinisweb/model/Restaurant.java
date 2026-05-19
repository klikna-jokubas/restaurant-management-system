package com.example.kursinisweb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "restaurants")
public class Restaurant extends BasicUser {

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    protected List<Cuisine> menu;

    @OneToMany(mappedBy = "restaurant")
    @JsonIgnore
    protected List<FoodOrder> foodOrders;

    public Restaurant() {
        super();
        this.userType = "restaurant";
    }

    @Override
    public String toString() {
        return name + " (" + address + ")";
    }

}
