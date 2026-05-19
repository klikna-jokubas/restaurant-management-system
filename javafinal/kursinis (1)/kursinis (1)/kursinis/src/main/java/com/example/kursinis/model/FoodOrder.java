package com.example.kursinis.model;

import jakarta.persistence.*;
import lombok.*;
import jakarta.persistence.FetchType;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "food_orders")
public class FoodOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;

    protected String name;
    protected Double price;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    protected BasicUser buyer;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    protected Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    protected Driver driver;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    protected OrderStatus status = OrderStatus.NEW;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "order_cuisine",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "cuisine_id")
    )
    protected List<Cuisine> cuisineList;


    @OneToOne(mappedBy = "foodOrder", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    protected Chat chat;
}
