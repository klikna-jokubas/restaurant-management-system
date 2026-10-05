package com.example.kursinisweb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    private String description;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    @JsonIgnore
    protected Restaurant restaurant;

    @ManyToMany(mappedBy = "cuisineList")
    @JsonIgnore
    protected List<FoodOrder> orderList;

    @Override
    public String toString() {
        String desc = (description != null) ? description : "";
        return name + " - " + price + "€\n" + desc;
    }
}
