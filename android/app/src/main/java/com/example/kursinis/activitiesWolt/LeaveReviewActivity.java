package com.example.kursinis.activitiesWolt;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kursinis.R;
import com.example.kursinis.utils.Constants;
import com.example.kursinis.utils.RestOperations;
import com.google.gson.JsonObject;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LeaveReviewActivity extends AppCompatActivity {

    private int orderId, restaurantId, driverId, authorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_review);

        orderId = getIntent().getIntExtra("orderId", 0);
        restaurantId = getIntent().getIntExtra("restaurantId", 0);
        driverId = getIntent().getIntExtra("driverId", 0);
        authorId = getIntent().getIntExtra("authorId", 0);

        Log.d("ORDER_TEST", "ORDER ID = " + orderId);

        Spinner starsSpin = findViewById(R.id.starsSpinner);
        EditText commentBox = findViewById(R.id.commentBox);

        ArrayAdapter<String> starsAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"1", "2", "3", "4", "5"}
        );
        starsSpin.setAdapter(starsAdapter);

        findViewById(R.id.submitReviewBtn).setOnClickListener(v -> {

            Executor executor = Executors.newSingleThreadExecutor();

            JsonObject json = new JsonObject();
            json.addProperty("authorId", authorId);
            json.addProperty("orderId", orderId);
            json.addProperty("stars", starsSpin.getSelectedItem().toString());
            json.addProperty("comment", commentBox.getText().toString());

            if (driverId != 0)
                json.addProperty("driverId", driverId);

            if (restaurantId != 0)
                json.addProperty("restaurantId", restaurantId);

            executor.execute(() -> {
                try {
                    RestOperations.sendPost(Constants.ADD_REVIEW, json.toString());
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Review submitted!", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                } catch (Exception e) { e.printStackTrace(); }
            });
        });
    }
}
