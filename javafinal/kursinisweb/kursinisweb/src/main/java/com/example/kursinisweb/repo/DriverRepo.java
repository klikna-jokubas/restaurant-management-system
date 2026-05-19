package com.example.kursinisweb.repo;

import com.example.kursinisweb.model.Driver;
import com.example.kursinisweb.model.FoodOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DriverRepo extends JpaRepository<Driver, Integer> {


}
