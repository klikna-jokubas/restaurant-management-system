package com.example.kursinisweb.controllers;

import com.example.kursinisweb.model.*;
import com.example.kursinisweb.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/rating/")
public class RatingController {

    @Autowired
    private RatingRepo ratingRepo;

    @Autowired
    private FoodOrderRepo foodOrderRepo;

    @Autowired
    private BasicUserRepo basicUserRepo;

    @Autowired
    private DriverRepo driverRepo;

    @Autowired
    private RestaurantRepo restaurantRepo;

    @PostMapping("add")
    public Rating addRating(@RequestBody Map<String, Object> data) {

        Rating r = new Rating();

        int authorId = Integer.parseInt(data.get("authorId").toString());
        int stars = Integer.parseInt(data.get("stars").toString());
        String comment = data.get("comment").toString();

        r.setStars(stars);
        r.setComment(comment);
        r.setDateCreated(LocalDate.now());

        // Author
        BasicUser author = basicUserRepo.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        r.setAuthor(author);

        // Restaurant target
        if (data.get("restaurantId") != null) {
            int restId = Integer.parseInt(data.get("restaurantId").toString());
            Restaurant rest = restaurantRepo.findById(restId)
                    .orElseThrow(() -> new RuntimeException("Restaurant not found"));
            r.setRestaurant(rest);
        }

        // Driver target
        if (data.get("driverId") != null) {
            int driverId = Integer.parseInt(data.get("driverId").toString());
            Driver driver = driverRepo.findById(driverId)
                    .orElseThrow(() -> new RuntimeException("Driver not found"));
            r.setDriver(driver);
        }

        return ratingRepo.save(r);
    }


}