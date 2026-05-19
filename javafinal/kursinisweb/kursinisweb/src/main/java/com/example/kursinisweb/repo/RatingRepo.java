package com.example.kursinisweb.repo;

import com.example.kursinisweb.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepo extends JpaRepository<Rating, Integer> {

    List<Rating> findAllByRestaurantId(int restaurantId);
    List<Rating> findAllByDriverId(int driverId);
    List<Rating> findAllByOrderId(int orderId);
}
