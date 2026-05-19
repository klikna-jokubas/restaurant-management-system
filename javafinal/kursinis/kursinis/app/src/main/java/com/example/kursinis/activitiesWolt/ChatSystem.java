package com.example.kursinis.activitiesWolt;

import static com.example.kursinis.utils.Constants.GET_MESSAGES_BY_ORDER;
import static com.example.kursinis.utils.Constants.MARK_ORDER_DELIVERED;
import static com.example.kursinis.utils.Constants.SEND_MESSAGE;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import com.example.kursinis.R;
import com.example.kursinis.utils.LocalDateAdapter;
import com.example.kursinis.utils.RestOperations;
import com.example.kursinis.model.Review;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ChatSystem extends AppCompatActivity {

    private int orderId;
    private int userId;
    private int restaurantId;
    private int driverId;
    private String orderStatus;

    private Button btnDeliver, btnRateRest, btnRateDriver, btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_system);

        Intent intent = getIntent();

        orderId = intent.getIntExtra("orderId", 0);
        userId = intent.getIntExtra("userId", 0);
        restaurantId = intent.getIntExtra("restaurantId", 0);
        driverId = intent.getIntExtra("driverId", -1);
        orderStatus = intent.getStringExtra("status");

        btnDeliver = findViewById(R.id.btnMarkDelivered);
        btnRateRest = findViewById(R.id.rateRestaurantBtn);
        btnRateDriver = findViewById(R.id.rateDriverBtn);
        btnSend = findViewById(R.id.button5);

        boolean isDriver = getIntent().getBooleanExtra("isDriver", false);

        if (isDriver) btnDeliver.setVisibility(View.VISIBLE);

        if ("DELIVERED".equals(orderStatus) || "COMPLETED".equals(orderStatus)) {

            btnSend.setEnabled(false);

            btnRateRest.setVisibility(View.VISIBLE);

            if (driverId != -1)
                btnRateDriver.setVisibility(View.VISIBLE);
        }

        loadMessages();
    }

    private void loadMessages() {
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                String response = RestOperations.sendGet(GET_MESSAGES_BY_ORDER + orderId);
                System.out.println(response);
                handler.post(() -> {
                    try {
                        if (!response.equals("Error")) {
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
                            Gson gsonMessages = gsonBuilder.setPrettyPrinting().create();
                            Type messagesListType = new TypeToken<List<Review>>() {
                            }.getType();
                            List<Review> messagesListFromJson = gsonMessages.fromJson(response, messagesListType);
                            ListView messagesListElement = findViewById(R.id.messageList);
                            ArrayAdapter<Review> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messagesListFromJson);
                            messagesListElement.setAdapter(adapter);
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

    public void sendMessage(View view) {
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        TextView messageBody = findViewById(R.id.bodyField);

        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userId", userId);
        jsonObject.addProperty("orderId", orderId);
        jsonObject.addProperty("messageText", messageBody.getText().toString());

        String message = gson.toJson(jsonObject);

        executor.execute(() -> {
            try {
                String response = RestOperations.sendPost(SEND_MESSAGE, message);
                handler.post(() -> {
                    if (response.equals("CHAT_CLOSED")) {
                        Toast.makeText(this, "Chat is closed for completed order.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    loadMessages();
                });
                System.out.println(response);
                handler.post(() -> {
                    try {
                        if (!response.equals("Error")) {
                            loadMessages();
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
    public void markDelivered(View view) {
        JsonObject json = new JsonObject();
        json.addProperty("orderId", orderId);

        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                RestOperations.sendPost(MARK_ORDER_DELIVERED, json.toString());

                handler.post(() -> {
                    Toast.makeText(this, "Order delivered!", Toast.LENGTH_SHORT).show();
                    finish(); // close chat
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    public void rateRestaurant(View view) {
        Intent i = new Intent(this, LeaveReviewActivity.class);
        i.putExtra("restaurantId", restaurantId);
        i.putExtra("authorId", userId);
        startActivity(i);
    }

    public void rateDriver(View view) {
        Intent i = new Intent(this, LeaveReviewActivity.class);
        i.putExtra("driverId", driverId);
        i.putExtra("authorId", userId);
        startActivity(i);
    }


}

