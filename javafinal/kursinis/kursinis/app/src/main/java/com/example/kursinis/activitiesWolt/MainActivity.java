package com.example.kursinis.activitiesWolt;

import static com.example.kursinis.utils.Constants.VALIDATE_USER_URL;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kursinis.R;
import com.example.kursinis.utils.RestOperations;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void validateUser(View view) {
        TextView loginField = findViewById(R.id.loginField);
        TextView passwordField = findViewById(R.id.passwordField);

        Gson gson = new Gson();
        JsonObject data = new JsonObject();
        data.addProperty("login", loginField.getText().toString());
        data.addProperty("password", passwordField.getText().toString());
        String info = gson.toJson(data);


        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());


        executor.execute(() -> {
            try {
                String response = RestOperations.sendPost(VALIDATE_USER_URL, info);
                handler.post(() -> {

                    if (response == null ||
                            response.equals("") ||
                            response.equals("null") ||
                            response.equals("Error")) {

                        Toast.makeText(MainActivity.this,
                                "Wrong login or password",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Intent intent = new Intent(MainActivity.this, WoltMain.class);
                    intent.putExtra("userJsonObject", response);
                    startActivity(intent);
                });


            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void openRegisterPage(View view) {
        Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
        startActivity(intent);
    }
}