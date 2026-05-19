package com.example.kursinisweb.controllers;

import com.example.kursinisweb.model.*;
import com.example.kursinisweb.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController

public class FoodOrderController {

    @Autowired
    private FoodOrderRepo foodOrderRepo;

    @Autowired
    private ChatRepo chatRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BasicUserRepo basicUserRepo;

    @Autowired
    private RestaurantRepo restaurantRepo;

    @Autowired
    private CuisineRepo cuisineRepo;

    @Autowired
    private DriverRepo driverRepo;


    @GetMapping("getAllFoodOrders")
    public @ResponseBody Iterable<FoodOrder> getAllFoodOrders(){
        return foodOrderRepo.findAll();
    }

    @GetMapping(value = "getOrderByUserId/{id}")
    public Iterable<FoodOrder> getByUserId(@PathVariable int id){

        return foodOrderRepo.getFoodOrdersByBuyerId(id);
    }
    @GetMapping(value = "getMessagesForOrder/{orderId}")
    public @ResponseBody Iterable<Review> getMessagesForOrder(@PathVariable int orderId){
        Chat chat = chatRepo.getChatByFoodOrderId(orderId);

        if (chat == null){
            return List.of();
        }
        return chat.getMessages();
    }

    @PostMapping("insertOrder")
    public @ResponseBody FoodOrder insertOrder(@RequestBody Map<String, String> data) {

        int userId = Integer.parseInt(data.get("userId"));
        int restaurantId = Integer.parseInt(data.get("restaurantId"));
        int cuisineId = Integer.parseInt(data.get("cuisineId"));

        BasicUser buyer = basicUserRepo.findById(userId).orElseThrow();
        Restaurant rest = restaurantRepo.findById(restaurantId).orElseThrow();
        Cuisine cuisine = cuisineRepo.findById(cuisineId).orElseThrow();

        FoodOrder order = new FoodOrder();
        order.setBuyer(buyer);
        order.setRestaurant(rest);
        order.setName(cuisine.getName());
        order.setPrice(cuisine.getPrice());
        order.setCuisineList(List.of(cuisine));
        order.setStatus(OrderStatus.NEW);

        return foodOrderRepo.save(order);
    }


    @GetMapping("driver/orders/new")
    public List<FoodOrder> getNewOrders() {
        return foodOrderRepo.findAllByStatusAndDriverIsNull(OrderStatus.MAKING);
    }

    @PostMapping("driver/orders/take")
    public String takeOrder(@RequestBody Map<String, String> body) {

        int orderId = Integer.parseInt(body.get("orderId"));
        int driverId = Integer.parseInt(body.get("driverId"));

        FoodOrder order = foodOrderRepo.getReferenceById(orderId);
        Driver driver = driverRepo.getReferenceById(driverId);

        if (order.getDriver() != null) return "Already taken";

        order.setDriver(driver);
        order.setStatus(OrderStatus.IN_PROGRESS);

        foodOrderRepo.save(order);

        return "OK";
    }



    @GetMapping("driver/orders/available")
    public List<FoodOrder> getAvailableOrders() {
        return foodOrderRepo.findAll().stream()
                .filter(o -> o.getDriver() == null)
                .filter(o ->
                        o.getStatus() == OrderStatus.NEW ||
                                o.getStatus() == OrderStatus.MAKING
                )
                .toList();
    }

}
