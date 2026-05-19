package com.example.kursinis.activitiesWolt;

import static com.example.kursinis.utils.Constants.GET_ALL_RESTAURANTS_URL;
import static com.example.kursinis.utils.Constants.GET_NEW_ORDERS_URL;
import static com.example.kursinis.utils.Constants.TAKE_ORDER_URL;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kursinis.R;
import com.example.kursinis.model.BasicUser;
import com.example.kursinis.model.Driver;
import com.example.kursinis.model.FoodOrder;
import com.example.kursinis.model.Restaurant;
import com.example.kursinis.model.User;
import com.example.kursinis.utils.RestOperations;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class WoltMain extends AppCompatActivity {

    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_wolt_main);

        Intent intent = getIntent();
        String userInfo = intent.getStringExtra("userJsonObject");

        Gson gson = new Gson();
        User temp = gson.fromJson(userInfo, User.class);

        if ("client".equals(temp.userType)) {
            currentUser = gson.fromJson(userInfo, BasicUser.class);
        }
        else if ("driver".equals(temp.userType)) {
            currentUser = gson.fromJson(userInfo, Driver.class);
        }
        else {
            currentUser = temp;
        }




        System.out.println("Logged User: " + userInfo);
        System.out.println("userType = " + currentUser.userType);

        switch (currentUser.userType) {

            case "driver":
                loadDriverUI();
                return;

            case "restaurant":
                Toast.makeText(this,
                        "Restaurant funkcijos prieinamos tik Desktop versijoje",
                        Toast.LENGTH_LONG).show();
                return;

            case "admin":
                Toast.makeText(this,
                        "Admin funkcijos tik per Desktop",
                        Toast.LENGTH_LONG).show();
                return;

            default:
                loadClientUI();
                break;
        }
    }

    private void loadClientUI() {

        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                String response = RestOperations.sendGet(GET_ALL_RESTAURANTS_URL);

                handler.post(() -> {
                    if (!"Error".equals(response)) {

                        Type listType = new TypeToken<List<Restaurant>>() {}.getType();
                        List<Restaurant> restaurants =
                                new Gson().fromJson(response, listType);

                        ListView list = findViewById(R.id.restaurantList);

                        ArrayAdapter adapter = new ArrayAdapter(
                                this,
                                android.R.layout.simple_list_item_1,
                                restaurants
                        );

                        list.setAdapter(adapter);

                        list.setOnItemClickListener((parent, view, pos, id) -> {

                            int restId = restaurants.get(pos).getId();

                            Intent toMenu = new Intent(this, MenuActivity.class);
                            toMenu.putExtra("restaurantId", restId);
                            toMenu.putExtra("userId", currentUser.getId());
                            startActivity(toMenu);
                        });
                    }
                });

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void loadDriverUI() {

        setContentView(R.layout.activity_driver);

        ListView lv = findViewById(R.id.driverOrdersList);

        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                String response = RestOperations.sendGet(GET_NEW_ORDERS_URL);

                handler.post(() -> {
                    if (!"Error".equals(response)) {

                        Type listType = new TypeToken<List<FoodOrder>>() {}.getType();
                        List<FoodOrder> orders =
                                new Gson().fromJson(response, listType);

                        ArrayAdapter adapter = new ArrayAdapter(
                                this,
                                android.R.layout.simple_list_item_1,
                                orders
                        );

                        lv.setAdapter(adapter);

                        lv.setOnItemClickListener((p, v, pos, id) -> {
                            FoodOrder order = orders.get(pos);


                            String status = order.getStatus().toString().toUpperCase().trim();

                            if (!status.equals("MAKING")) {
                                Toast.makeText(this, "Order is not ready yet!", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            takeOrder(order);
                        });
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    // DRIVER TAKES ORDER
    private void takeOrder(FoodOrder order) {

        JsonObject json = new JsonObject();
        json.addProperty("orderId", order.getId());
        json.addProperty("driverId", currentUser.getId());

        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {

            try {
                RestOperations.sendPost(TAKE_ORDER_URL, json.toString());

                handler.post(() -> {
                    Toast.makeText(this,
                            "Order #" + order.getId() + " picked!",
                            Toast.LENGTH_SHORT).show();

                    loadDriverUI();
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void viewPurchaseHistory(View view) {
        Intent intent = new Intent(WoltMain.this, MyOrders.class);
        intent.putExtra("id", currentUser.getId());
        startActivity(intent);
    }

    public void viewMyAccount(View view) {

        Intent intent = new Intent(WoltMain.this, MyInfoActivity.class);

        Gson gson = new Gson();
        intent.putExtra("userData", gson.toJson(currentUser));

        startActivity(intent);
    }

    public void viewDriverOrders(View view) {
        Intent i = new Intent(this, DriverOrdersActivity.class);
        i.putExtra("driverId", currentUser.getId());
        startActivity(i);
    }
    public void viewDriverMyInfo(View view) {
        Intent i = new Intent(this, MyInfoActivity.class);

        Gson gson = new Gson();
        i.putExtra("userData", gson.toJson(currentUser));

        startActivity(i);
    }


}
