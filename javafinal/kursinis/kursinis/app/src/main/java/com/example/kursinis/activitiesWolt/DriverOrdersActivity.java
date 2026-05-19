package com.example.kursinis.activitiesWolt;

import static com.example.kursinis.utils.Constants.GET_DRIVER_ORDERS_URL;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kursinis.R;
import com.example.kursinis.model.FoodOrder;
import com.example.kursinis.utils.RestOperations;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class DriverOrdersActivity extends AppCompatActivity {

    private int driverId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);

        driverId = getIntent().getIntExtra("driverId", 0);

        loadDriverOrders();
    }

    private void loadDriverOrders() {
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                String response = RestOperations.sendGet(GET_DRIVER_ORDERS_URL + driverId);

                handler.post(() -> {
                    Type listType = new TypeToken<List<FoodOrder>>(){}.getType();
                    List<FoodOrder> orders = new Gson().fromJson(response, listType);

                    ListView list = findViewById(R.id.driverOrdersList);

                    ArrayAdapter adapter = new ArrayAdapter(
                            this,
                            android.R.layout.simple_list_item_1,
                            orders
                    );

                    list.setAdapter(adapter);

                    list.setOnItemClickListener((parent, view, pos, id) -> {
                        FoodOrder selected = orders.get(pos);

                        Intent i = new Intent(this, ChatSystem.class);
                        i.putExtra("orderId", selected.getId());
                        i.putExtra("userId", driverId);
                        i.putExtra("isDriver", true);
                        startActivity(i);
                    });
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}

