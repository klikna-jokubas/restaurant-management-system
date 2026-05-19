package com.example.kursinisweb.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private int stars;
    private String comment;

    @ManyToOne
    private BasicUser author;

    @ManyToOne
    private Driver driver;

    @ManyToOne
    private Restaurant restaurant;

    @ManyToOne
    private FoodOrder order;

    private LocalDate dateCreated;

}
