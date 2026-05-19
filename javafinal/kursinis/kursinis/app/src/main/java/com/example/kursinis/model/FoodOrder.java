package com.example.kursinis.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class FoodOrder {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("price")
    private double price;

    @SerializedName("status")
    private OrderStatus status;

    @SerializedName("buyer")
    private BasicUser buyer;

    @SerializedName("restaurant")
    private Restaurant restaurant;

    @SerializedName("driver")
    private Driver driver;

    @SerializedName("cuisineList")
    private List<Cuisine> cuisineList;


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public BasicUser getBuyer() {
        return buyer;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public Driver getDriver() {
        return driver;
    }

    public List<Cuisine> getCuisineList() {
        return cuisineList;
    }

    public int getRestaurantId() {
        return restaurant != null ? restaurant.getId() : 0;
    }

    @Override
    public String toString() {
        return "Order #" + id +
                "\nName: " + name +
                "\nPrice: " + price +
                "\nStatus: " + status;
    }
}
