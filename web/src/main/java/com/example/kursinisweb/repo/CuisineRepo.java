package com.example.kursinisweb.repo;

import com.example.kursinisweb.model.Cuisine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CuisineRepo extends JpaRepository<Cuisine, Integer> {
}
