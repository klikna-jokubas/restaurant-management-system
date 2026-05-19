package com.example.kursinisweb.controllers;

import com.example.kursinisweb.model.Cuisine;
import com.example.kursinisweb.model.Restaurant;
import com.example.kursinisweb.repo.CuisineRepo;
import com.example.kursinisweb.repo.RestaurantRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RestaurantControlller {

    @Autowired
    private CuisineRepo cuisineRepo;

    @Autowired
    private RestaurantRepo restaurantRepo;


    @GetMapping("restaurant/{id}/menu")
    public @ResponseBody List<Cuisine> getMenu(@PathVariable int id) {

        Restaurant restaurant = restaurantRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        return restaurant.getMenu();
    }
}
