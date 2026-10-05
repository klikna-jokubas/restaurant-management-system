package com.example.kursinis.activitiesWolt;

import static com.example.kursinis.utils.Constants.REGISTER_USER_URL;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
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

public class RegistrationActivity extends AppCompatActivity {

    private RadioGroup userTypeGroup;
    private LinearLayout driverFields;

    private EditText loginField, passwordField, nameField, surnameField, phoneField, addressField;
    private EditText licenceField, bdateField;
    private Spinner vehicleSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.register_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userTypeGroup = findViewById(R.id.userTypeGroup);
        driverFields = findViewById(R.id.driverFields);

        loginField = findViewById(R.id.loginField);
        passwordField = findViewById(R.id.passwordField);
        nameField = findViewById(R.id.nameField);
        surnameField = findViewById(R.id.surnameField);
        phoneField = findViewById(R.id.phoneField);
        addressField = findViewById(R.id.addressField);

        licenceField = findViewById(R.id.licenceField);
        bdateField = findViewById(R.id.birthDateField);
        vehicleSpinner = findViewById(R.id.vehicleTypeSpinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"CAR", "SCOOTER", "BIKE", "BY_FOOT"}
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehicleSpinner.setAdapter(adapter);

        userTypeGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioDriver) {
                driverFields.setVisibility(View.VISIBLE);
            } else {
                driverFields.setVisibility(View.GONE);
            }
        });
    }


    public void registerUser(View view) {

        Gson gson = new Gson();
        JsonObject data = new JsonObject();

        data.addProperty("login", loginField.getText().toString());
        data.addProperty("password", passwordField.getText().toString());
        data.addProperty("name", nameField.getText().toString());
        data.addProperty("surname", surnameField.getText().toString());
        data.addProperty("phoneNumber", phoneField.getText().toString());
        data.addProperty("address", addressField.getText().toString());


        if (userTypeGroup.getCheckedRadioButtonId() == R.id.radioDriver) {
            data.addProperty("licence", licenceField.getText().toString());
            data.addProperty("bDate", bdateField.getText().toString());
            data.addProperty("vehicleType", vehicleSpinner.getSelectedItem().toString());
            data.addProperty("userType", "driver");

        } else {
            data.addProperty("isAdmin", false);
            data.addProperty("userType", "client");
        }

        String jsonToSend = gson.toJson(data);

        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                String response = RestOperations.sendPost(REGISTER_USER_URL, jsonToSend);

                handler.post(() -> {
                    if (response == null || response.equals("Error") || response.equals("")) {
                        Toast.makeText(RegistrationActivity.this,
                                "Registration failed",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Toast.makeText(RegistrationActivity.this,
                            "Registration successful!",
                            Toast.LENGTH_SHORT).show();

                    finish();
                });

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
