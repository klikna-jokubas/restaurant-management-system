package com.example.kursinis.activitiesWolt;

import static com.example.kursinis.utils.Constants.UPDATE_USER;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kursinis.R;
import com.example.kursinis.model.BasicUser;
import com.example.kursinis.model.Driver;
import com.example.kursinis.model.User;
import com.example.kursinis.model.VehicleType;
import com.example.kursinis.utils.RestOperations;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MyInfoActivity extends AppCompatActivity {

    private User currentUser;

    private EditText txtLogin, txtName, txtSurname, txtPhone, txtAddress, txtPassword;
    private Button btnSave;


    Spinner vehicleSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);

        String userJson = getIntent().getStringExtra("userData");
        Gson gson = new Gson();

        try {
            org.json.JSONObject raw = new org.json.JSONObject(userJson);
            String type = raw.getString("userType");

            switch (type) {
                case "client":
                    currentUser = gson.fromJson(userJson, BasicUser.class);
                    break;

                case "driver":
                    currentUser = gson.fromJson(userJson, Driver.class);
                    break;

                default:
                    currentUser = gson.fromJson(userJson, User.class);
            }

        } catch (Exception e) {
            e.printStackTrace();
            currentUser = gson.fromJson(userJson, User.class);
        }


        vehicleSpinner = findViewById(R.id.spinnerVehicle);

        if (currentUser instanceof Driver) {

            vehicleSpinner.setVisibility(View.VISIBLE);

            ArrayAdapter<String> vehicleAdapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_item,
                    new String[]{"CAR", "SCOOTER", "BIKE", "BY_FOOT"}
            );

            vehicleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            vehicleSpinner.setAdapter(vehicleAdapter);

            // nustatom current value
            Driver driver = (Driver) currentUser;
            int index = vehicleAdapter.getPosition(driver.getVehicleType().name());
            vehicleSpinner.setSelection(index);
        }


        txtLogin = findViewById(R.id.txtLogin);
        txtPassword = findViewById(R.id.txtPassword);
        txtName = findViewById(R.id.txtName);
        txtSurname = findViewById(R.id.txtSurname);
        txtPhone = findViewById(R.id.txtPhone);
        txtAddress = findViewById(R.id.txtAddress);

        btnSave = findViewById(R.id.btnSave);

        loadData();

        btnSave.setOnClickListener(v -> saveChanges());
    }

    private void loadData() {
        txtLogin.setText(currentUser.getLogin());
        txtPassword.setText("");

        txtName.setText(currentUser.getName());
        txtSurname.setText(currentUser.getSurname());
        txtPhone.setText(currentUser.getPhoneNumber());
        txtAddress.setText(currentUser.getAddress());
    }

    private void saveChanges() {

        JsonObject json = new JsonObject();

        json.addProperty("id", currentUser.getId());
        json.addProperty("name", txtName.getText().toString());
        json.addProperty("surname", txtSurname.getText().toString());
        json.addProperty("phoneNumber", txtPhone.getText().toString());

        if (!txtPassword.getText().toString().isEmpty()) {
            json.addProperty("password", txtPassword.getText().toString());
        }

        if (currentUser instanceof BasicUser) {
            json.addProperty("address", txtAddress.getText().toString());
        }

        if (currentUser instanceof Driver) {
            String selectedVehicle = vehicleSpinner.getSelectedItem().toString();
            json.addProperty("vehicleType", selectedVehicle);
        }

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                RestOperations.sendPut(UPDATE_USER, json.toString());

                runOnUiThread(() ->
                        Toast.makeText(this, "Informacija atnaujinta!", Toast.LENGTH_SHORT).show()
                );

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }



}
