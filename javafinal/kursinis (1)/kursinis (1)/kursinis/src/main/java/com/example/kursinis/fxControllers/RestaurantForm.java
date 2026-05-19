package com.example.kursinis.fxControllers;

import com.example.kursinis.hibernateControl.GenericHibernate;
import com.example.kursinis.model.Restaurant;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class RestaurantForm {

    @FXML
    private TextField txtName;
    @FXML
    private TextField txtAddress;
    @FXML
    private TextField txtPhone;
    @FXML
    private Label lblError;

    private final GenericHibernate gh = new GenericHibernate();
    private Restaurant editingRestaurant;

    public void setData(Restaurant restaurant) {
        if (restaurant != null) {
            this.editingRestaurant = restaurant;
            txtName.setText(restaurant.getName());
            txtAddress.setText(restaurant.getAddress());
            txtPhone.setText(restaurant.getPhoneNumber());
        } else {
            this.editingRestaurant = new Restaurant();
        }
    }

    @FXML
    private void onSave() {
        lblError.setText("");

        String name    = txtName.getText();
        String address = txtAddress.getText();
        String phone   = txtPhone.getText();

        if (name == null || name.isBlank()) {
            lblError.setText("Pavadinimas privalomas");
            return;
        }
        if (address == null || address.isBlank()) {
            lblError.setText("Adresas privalomas");
            return;
        }
        if (phone != null && !phone.isBlank() && !phone.matches("\\+?\\d{8,12}")) {
            lblError.setText("Neteisingas telefono formatas");
            return;
        }

        editingRestaurant.setName(name);
        editingRestaurant.setAddress(address);
        editingRestaurant.setPhoneNumber(phone);

        try {
            if (editingRestaurant.getId() == 0) {
                gh.create(editingRestaurant);
            } else {
                gh.update(editingRestaurant);
            }
            new Alert(Alert.AlertType.INFORMATION, "Restoranas išsaugotas", ButtonType.OK).showAndWait();
            txtName.getScene().getWindow().hide();
        } catch (Exception e) {
            e.printStackTrace();
            lblError.setText("Klaida saugant");
        }
    }

    @FXML
    private void onCancel() {
        txtName.getScene().getWindow().hide();
    }
}
