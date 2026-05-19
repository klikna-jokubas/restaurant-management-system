package com.example.kursinisweb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @JsonIgnoreProperties({"myOrders", "myReviews", "feedback"})
    protected BasicUser buyer;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    @JsonIgnoreProperties({"foodOrders", "menu"})
    protected Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    @JsonIgnoreProperties({"myOrders"})
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
    @JsonIgnoreProperties({"orderList", "restaurant"})
    protected List<Cuisine> cuisineList;

    @OneToOne(mappedBy = "foodOrder", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnore
    protected Chat chat;
}
