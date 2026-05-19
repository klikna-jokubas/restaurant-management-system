package com.example.kursinis.fxControllers;

import com.example.kursinis.hibernateControl.GenericHibernate;
import com.example.kursinis.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;

public class OrderForm {

    @FXML
    private TextField txtName;
    @FXML
    private TextField txtPrice;

    @FXML
    private ComboBox<BasicUser> cmbBuyer;
    @FXML
    private ComboBox<Restaurant> cmbRestaurant;
    @FXML
    private ComboBox<Driver> cmbDriver;
    @FXML
    private ComboBox<OrderStatus> cmbStatus;

    @FXML
    private ListView<Cuisine> listCuisine;
    @FXML
    private Label lblError;

    private FoodOrder editingOrder;
    private ObservableList<BasicUser> allUsers;

    private final GenericHibernate gh = new GenericHibernate();

    public void setData(FoodOrder order,
                        ObservableList<BasicUser> allUsers) {

        this.allUsers = allUsers;
        cmbBuyer.setItems(allUsers);

        List<Restaurant> restaurantList = gh.getAllRecords(Restaurant.class);
        List<Driver> driverList = gh.getAllRecords(Driver.class);
        List<Cuisine> cuisineList = gh.getAllRecords(Cuisine.class);

        cmbRestaurant.setItems(FXCollections.observableArrayList(restaurantList));
        cmbDriver.setItems(FXCollections.observableArrayList(driverList));

        cmbStatus.setItems(FXCollections.observableArrayList(OrderStatus.values()));

        listCuisine.setItems(FXCollections.observableArrayList(cuisineList));
        listCuisine.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        if (order != null) {
            this.editingOrder = order;
            txtName.setText(order.getName());
            txtPrice.setText(order.getPrice() != null ? order.getPrice().toString() : "");
            cmbBuyer.setValue(order.getBuyer());
            cmbRestaurant.setValue(order.getRestaurant());
            cmbDriver.setValue(order.getDriver());
            cmbStatus.setValue(order.getStatus() != null ? order.getStatus() : OrderStatus.NEW);

            if (order.getCuisineList() != null) {
                listCuisine.getSelectionModel().clearSelection();
                for (Cuisine c : order.getCuisineList()) {
                    listCuisine.getSelectionModel().select(c);
                }
            }
        } else {
            this.editingOrder = new FoodOrder();
            cmbStatus.setValue(OrderStatus.NEW);
        }
    }

    @FXML
    private void onSave() {
        lblError.setText("");

        String name = txtName.getText();
        String priceStr = txtPrice.getText();

        if (name == null || name.isBlank()) {
            lblError.setText("Pavadinimas privalomas");
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
            if (price < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            lblError.setText("Kaina turi būti teigiamas skaičius");
            return;
        }

        BasicUser buyer = cmbBuyer.getValue();
        if (buyer == null) {
            lblError.setText("Pasirinkite pirkėją");
            return;
        }

        Restaurant restaurant = cmbRestaurant.getValue();
        if (restaurant == null) {
            lblError.setText("Pasirinkite restoraną");
            return;
        }

        Driver driver = cmbDriver.getValue();
        OrderStatus status = cmbStatus.getValue();
        if (status == null) {
            status = OrderStatus.NEW;
        }

        editingOrder.setName(name);
        editingOrder.setPrice(price);
        editingOrder.setBuyer(buyer);
        editingOrder.setRestaurant(restaurant);
        editingOrder.setDriver(driver);
        editingOrder.setStatus(status);

        List<Cuisine> selectedCuisines =
                new ArrayList<>(listCuisine.getSelectionModel().getSelectedItems());
        editingOrder.setCuisineList(selectedCuisines);

        try {
            if (editingOrder.getId() == 0) {
                gh.create(editingOrder);
            } else {
                gh.update(editingOrder);
            }

            new Alert(Alert.AlertType.INFORMATION,
                    "Užsakymas išsaugotas",
                    ButtonType.OK).showAndWait();

            txtName.getScene().getWindow().hide();

        } catch (Exception e) {
            e.printStackTrace();
            lblError.setText("Klaida saugant užsakymą į DB");
        }
    }

    @FXML
    private void onCancel() {
        txtName.getScene().getWindow().hide();
    }
}
