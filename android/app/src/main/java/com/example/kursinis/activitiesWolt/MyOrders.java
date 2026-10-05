package com.example.kursinis.activitiesWolt;

import static com.example.kursinis.utils.Constants.GET_ORDERS_BY_USER;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kursinis.R;
import com.example.kursinis.utils.RestOperations;
import com.example.kursinis.model.FoodOrder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MyOrders extends AppCompatActivity {

    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_orders);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Intent intent = getIntent();
        userId = intent.getIntExtra("id", 0);

        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                String response = RestOperations.sendGet(GET_ORDERS_BY_USER + userId);
                System.out.println(response);
                handler.post(() -> {
                    try {
                        if (!response.equals("Error")) {
                            Type ordersListType = new TypeToken<List<FoodOrder>>() {
                            }.getType();
                            final List<FoodOrder> ordersListFromJson = new com.google.gson.Gson().fromJson(response, ordersListType);
                            ListView ordersListElement = findViewById(R.id.myOrderList);
                            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ordersListFromJson);
                            ordersListElement.setAdapter(adapter);

                            ordersListElement.setOnItemClickListener((parent, view, pos, id) -> {

                                FoodOrder selected = ordersListFromJson.get(pos);

                                Log.e("ORDER", "Selected order ID = " + selected.getId());
                                Toast.makeText(MyOrders.this, "Order ID = " + selected.getId(), Toast.LENGTH_LONG).show();


                                if ("DELIVERED".equals(selected.getStatus())) {
                                    Intent r = new Intent(MyOrders.this, LeaveReviewActivity.class);
                                    r.putExtra("orderId", selected.getId());
                                    r.putExtra("authorId", userId);

                                    Log.d("ORDER_TEST", "ORDER ID = " + selected.getId());

                                    if (selected.getRestaurant() != null)
                                        r.putExtra("restaurantId", selected.getRestaurant().getId());

                                    if (selected.getDriver() != null)
                                        r.putExtra("driverId", selected.getDriver().getId());

                                    startActivity(r);
                                    return;
                                }


                                Intent intentChat = new Intent(MyOrders.this, ChatSystem.class);

                                intentChat.putExtra("orderId", selected.getId());
                                intentChat.putExtra("userId", userId);
                                intentChat.putExtra("driverId",
                                        selected.getDriver() != null ? selected.getDriver().getId() : 0
                                );
                                intentChat.putExtra("restaurantId", selected.getRestaurantId());
                                intentChat.putExtra("status", selected.getStatus());

                                startActivity(intentChat);
                            });



                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

}