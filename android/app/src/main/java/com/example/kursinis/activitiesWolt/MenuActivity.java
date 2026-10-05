package com.example.kursinis.activitiesWolt;

import static com.example.kursinis.utils.Constants.GET_MENU_BY_RESTAURANT;
import static com.example.kursinis.utils.Constants.INSERT_ORDER_URL;  // <-- turi pridėti

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kursinis.R;
import com.example.kursinis.model.Cuisine;
import com.example.kursinis.utils.RestOperations;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MenuActivity extends AppCompatActivity {

    private int restaurantId;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        restaurantId = getIntent().getIntExtra("restaurantId", 0);
        userId = getIntent().getIntExtra("userId", 0);

        loadMenu();
    }

    private void loadMenu() {
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                String response = RestOperations.sendGet(
                        GET_MENU_BY_RESTAURANT + restaurantId + "/menu"
                );

                handler.post(() -> {
                    try {
                        if (!response.equals("Error")) {

                            Type listType = new TypeToken<List<Cuisine>>() {}.getType();
                            List<Cuisine> menu = new Gson().fromJson(response, listType);

                            ListView menuList = findViewById(R.id.menuList);
                            ArrayAdapter<Cuisine> adapter = new ArrayAdapter<>(
                                    this,
                                    android.R.layout.simple_list_item_1,
                                    menu
                            );
                            menuList.setAdapter(adapter);

                            menuList.setOnItemClickListener((parent, view, position, id) -> {
                                Cuisine selected = menu.get(position);
                                addToCart(selected);
                            });

                        }
                    } catch (Exception e) { e.printStackTrace(); }
                });

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
    }

    private void addToCart(Cuisine cuisine) {

        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userId", userId);
        jsonObject.addProperty("restaurantId", restaurantId);
        jsonObject.addProperty("cuisineId", cuisine.getId());

        String json = gson.toJson(jsonObject);

        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                String response = RestOperations.sendPost(INSERT_ORDER_URL, json);

                handler.post(() -> {
                    Toast.makeText(MenuActivity.this,
                            cuisine.getName() + " You just bought food!",
                            Toast.LENGTH_SHORT).show();
                });

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
