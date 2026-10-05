package com.example.kursinisweb.repo;

import com.example.kursinisweb.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepo extends JpaRepository<Review, Integer> {
}
