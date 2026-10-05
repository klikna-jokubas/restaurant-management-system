package com.example.kursinisweb.repo;

import com.example.kursinisweb.model.FoodOrder;
import com.example.kursinisweb.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodOrderRepo extends JpaRepository<FoodOrder, Integer> {
    List<FoodOrder> getFoodOrdersByBuyerId(int id);

    List<FoodOrder> getFoodOrdersByDriverId(int id);

    List<FoodOrder> findAllByStatusAndDriverIsNull(OrderStatus status);

    List<FoodOrder> findAllByDriver_Id(int driverId);


}
