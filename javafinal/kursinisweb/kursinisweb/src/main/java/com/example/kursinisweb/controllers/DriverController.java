package com.example.kursinisweb.controllers;

import com.example.kursinisweb.model.Driver;
import com.example.kursinisweb.model.FoodOrder;
import com.example.kursinisweb.model.OrderStatus;
import com.example.kursinisweb.repo.DriverRepo;
import com.example.kursinisweb.repo.FoodOrderRepo;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Properties;

@RestController
public class DriverController {

    @Autowired
    private DriverRepo driverRepo;

    @Autowired
    private FoodOrderRepo foodOrderRepo;



    // GET ALL DRIVERS
    @GetMapping("getAllDrivers")
    public @ResponseBody Iterable<Driver> getAllDrivers() {
        return driverRepo.findAll();
    }


    // GET DRIVER BY ID
    @GetMapping("getDriver/{id}")
    public @ResponseBody Driver getDriver(@PathVariable int id) {
        return driverRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Driver not found"));
    }


    // INSERT DRIVER
    @PostMapping("insertDriver")
    public @ResponseBody Driver insertDriver(@RequestBody Driver driver) {
        driverRepo.save(driver);
        return driverRepo.findById(driver.getId()).orElse(null);
    }



    // UPDATE DRIVER BY ID
    @PutMapping("updateDriverById/{id}")
    public @ResponseBody Driver updateDriverById(
            @RequestBody String info,
            @PathVariable int id
    ) {
        Driver driver = driverRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        Gson gson = new Gson();
        Properties props = gson.fromJson(info, Properties.class);

        if (props.getProperty("name") != null)
            driver.setName(props.getProperty("name"));

        if (props.getProperty("surname") != null)
            driver.setSurname(props.getProperty("surname"));

        if (props.getProperty("phoneNumber") != null)
            driver.setPhoneNumber(props.getProperty("phoneNumber"));

        if (props.getProperty("licence") != null)
            driver.setLicence(props.getProperty("licence"));

        driverRepo.save(driver);
        return driverRepo.findById(id).orElse(null);
    }


    // DELETE DRIVER
    @DeleteMapping("deleteDriver/{id}")
    public @ResponseBody String deleteDriver(@PathVariable int id) {

        List<FoodOrder> orders = foodOrderRepo.getFoodOrdersByDriverId(id);
        for (FoodOrder o : orders) {
            o.setDriver(null);
        }
        foodOrderRepo.saveAll(orders);

        driverRepo.deleteById(id);

        boolean stillExists = driverRepo.findById(id).isPresent();

        return stillExists ? "fail on delete" : "success";
    }

    @GetMapping("driver/orders/{driverId}")
    public List<FoodOrder> getDriverOrders(@PathVariable int driverId) {
        return foodOrderRepo.findAllByDriver_Id(driverId);
    }

    @PostMapping("driver/order/deliver")
    public FoodOrder markDelivered(@RequestBody Map<String, Integer> data) {
        int orderId = data.get("orderId");

        FoodOrder order = foodOrderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(OrderStatus.DELIVERED);

        return foodOrderRepo.save(order);
    }


}
