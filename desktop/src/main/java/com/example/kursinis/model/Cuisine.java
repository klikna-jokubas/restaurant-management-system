package com.example.kursinis.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "cuisines")
public class Cuisine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;

    protected String name;

    protected Double price;

    protected boolean spicy = false;
    protected boolean vegan = false;

    protected String description;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    protected Restaurant restaurant;

    @ManyToMany(mappedBy = "cuisineList")
    protected List<FoodOrder> orderList;

    @Override
    public String toString() {
        return name + " (" + (price != null ? price : 0) + "€)";
    }
}
