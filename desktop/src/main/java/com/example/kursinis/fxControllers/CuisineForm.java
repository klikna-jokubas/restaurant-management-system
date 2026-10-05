package com.example.kursinis.fxControllers;

import com.example.kursinis.hibernateControl.GenericHibernate;
import com.example.kursinis.model.BasicUser;
import com.example.kursinis.model.Cuisine;
import com.example.kursinis.model.Restaurant;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class CuisineForm {

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPrice;

    @FXML
    private ComboBox<Restaurant> cmbRestaurant;

    @FXML
    private CheckBox chkSpicy;

    @FXML
    private CheckBox chkVegan;

    @FXML
    private TextArea txtDescription;

    @FXML
    private Label lblError;

    private final GenericHibernate gh = new GenericHibernate();

    private Cuisine editingCuisine;
    private BasicUser loggedUser;
    private List<Restaurant> allRestaurants;

    public void setData(Cuisine cuisine, BasicUser loggedUser) {

        this.loggedUser = loggedUser;

        if (cuisine == null) {
            this.editingCuisine = new Cuisine();
        } else {
            this.editingCuisine = cuisine;
        }

        if (loggedUser.isAdmin()) {
            allRestaurants = gh.getAllRecords(Restaurant.class);
            cmbRestaurant.setItems(FXCollections.observableArrayList(allRestaurants));
        }

        if (loggedUser.getUserType().equals("restaurant")) {

            Restaurant rest = (Restaurant) loggedUser;

            cmbRestaurant.setDisable(true);
            cmbRestaurant.setValue(rest);
            editingCuisine.setRestaurant(rest);
        }


        if (cuisine != null) {
            txtName.setText(cuisine.getName());
            txtPrice.setText("" + cuisine.getPrice());
            txtDescription.setText(cuisine.getDescription());
            chkSpicy.setSelected(cuisine.isSpicy());
            chkVegan.setSelected(cuisine.isVegan());

            if (loggedUser.isAdmin()) {
                cmbRestaurant.setValue(cuisine.getRestaurant());
            }
        }
    }


    @FXML
    private void onSave() {
        lblError.setText("");

        String name = txtName.getText();
        String priceStr = txtPrice.getText();
        Restaurant restaurant = cmbRestaurant.getValue();

        if (name == null || name.isBlank()) {
            lblError.setText("Pavadinimas privalomas");
            return;
        }
        if (restaurant == null) {
            lblError.setText("Pasirinkite restoraną");
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (Exception e) {
            lblError.setText("Kaina turi būti skaičius");
            return;
        }

        editingCuisine.setName(name);
        editingCuisine.setPrice(price);
        editingCuisine.setRestaurant(restaurant);
        editingCuisine.setSpicy(chkSpicy.isSelected());
        editingCuisine.setVegan(chkVegan.isSelected());

        editingCuisine.setDescription(
                txtDescription.getText() != null && !txtDescription.getText().isBlank()
                        ? txtDescription.getText()
                        : null
        );

        try {
            if (editingCuisine.getId() == 0) {
                gh.create(editingCuisine);
            } else {
                gh.update(editingCuisine);
            }

            new Alert(Alert.AlertType.INFORMATION,
                    "Patiekalas išsaugotas",
                    ButtonType.OK).showAndWait();

            txtName.getScene().getWindow().hide();

        } catch (Exception e) {
            e.printStackTrace();
            lblError.setText("Klaida saugant patiekalą");
        }
    }

    @FXML
    private void onCancel() {
        txtName.getScene().getWindow().hide();
    }
}
