package com.example.kursinisweb.controllers;

import com.example.kursinisweb.model.*;
import com.example.kursinisweb.repo.BasicUserRepo;
import com.example.kursinisweb.repo.RestaurantRepo;
import com.example.kursinisweb.repo.UserRepo;
import com.example.kursinisweb.util.PasswordUtil;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;
import java.util.Properties;

@RestController

public class UserController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RestaurantRepo restaurantRepo;
    @Autowired
    private BasicUserRepo basicUserRepo;


    @GetMapping("getAllUsers")
    public @ResponseBody Iterable<User> getAllUsers(){
        return userRepo.findAll();
    }
    @GetMapping("getAllRestaurants")
    public @ResponseBody Iterable<Restaurant> getAllRestaurants(){
        return restaurantRepo.findAll();
    }

    @PostMapping(value = "validateUser")
    public @ResponseBody User validateUser(@RequestBody String info) {

        Gson gson = new Gson();
        Properties properties = gson.fromJson(info, Properties.class);

        var login = properties.getProperty("login");
        var password = properties.getProperty("password");

        User user = userRepo.getUserByLogin(login);

        if (user == null) {
            return null;
        }

        if (!PasswordUtil.checkPassword(password, user.getPassword())) {
            return null;
        }

        return user;
    }


    @PutMapping("updateUser")
    public User updateUser(@RequestBody Map<String, Object> data) {

        Object idObj = data.get("id");
        int id;

        if (idObj instanceof Number)
            id = ((Number) idObj).intValue();
        else
            id = Integer.parseInt(idObj.toString());

        User existing = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // COMMON FIELDS
        if (data.containsKey("name")) existing.setName((String) data.get("name"));
        if (data.containsKey("surname")) existing.setSurname((String) data.get("surname"));
        if (data.containsKey("phoneNumber")) existing.setPhoneNumber((String) data.get("phoneNumber"));

        // HASHED PASSWORD UPDATE
        if (data.containsKey("password")) {
            String raw = (String) data.get("password");
            if (raw != null && !raw.isBlank()) {
                existing.setPassword(PasswordUtil.hashPassword(raw)); // ✔ hash
            }
        }

        // BASIC USER
        if (existing instanceof BasicUser basic) {
            if (data.containsKey("address")) {
                basic.setAddress((String) data.get("address"));
            }
        }

        // DRIVER
        if (existing instanceof Driver driver) {
            if (data.containsKey("vehicleType")) {
                driver.setVehicleType(VehicleType.valueOf((String) data.get("vehicleType")));
            }
        }

        return userRepo.save(existing);
    }



    @PutMapping(value = "updateUserById/{id}")
    public @ResponseBody User updateUserById(@RequestBody String info, @PathVariable int id){
        User user = userRepo.findById(id).orElseThrow(()-> new RuntimeException("User not found"));
        Gson gson = new Gson();
        Properties properties = gson.fromJson(info, Properties.class);
        var name = properties.getProperty("name");
        user.setName(name);
        userRepo.save(user);
        return userRepo.getReferenceById(user.getId());
    }


    @DeleteMapping(value = "deleteUser/{id}")
    public @ResponseBody String deleteUser(@PathVariable int id){
        userRepo.deleteById(id);
        User user = userRepo.findById(id).orElse(null);
        if(user != null) {
            return "fail on delete";
        }
        else {
            return "success";
        }
    }

    @PostMapping("registerUser")
    public @ResponseBody User registerUser(@RequestBody String info) {

        Gson gson = new Gson();
        Properties props = gson.fromJson(info, Properties.class);

        String userType = props.getProperty("userType");
        String login = props.getProperty("login");
        String password = props.getProperty("password");  // RAW (plain)
        String name = props.getProperty("name");
        String surname = props.getProperty("surname");
        String phone = props.getProperty("phoneNumber");
        String address = props.getProperty("address");

        if (userType == null) {
            throw new RuntimeException("Missing userType");
        }

        String hashedPassword = PasswordUtil.hashPassword(password);

        if (userType.equals("client")) {
            BasicUser client = new BasicUser();
            client.setUserType("client");
            client.setLogin(login);
            client.setPassword(hashedPassword); // ⬅ HASHED
            client.setName(name);
            client.setSurname(surname);
            client.setPhoneNumber(phone);
            client.setAddress(address);

            userRepo.save(client);
            return client;
        }

        if (userType.equals("driver")) {
            Driver driver = new Driver();
            driver.setUserType("driver");
            driver.setLogin(login);
            driver.setPassword(hashedPassword); // ⬅ HASHED
            driver.setName(name);
            driver.setSurname(surname);
            driver.setPhoneNumber(phone);
            driver.setAddress(address);

            driver.setLicence(props.getProperty("licence"));
            driver.setBDate(LocalDate.parse(props.getProperty("bDate")));
            driver.setVehicleType(VehicleType.valueOf(props.getProperty("vehicleType")));

            userRepo.save(driver);
            return driver;
        }

        throw new RuntimeException("Invalid userType: " + userType);
    }



}
