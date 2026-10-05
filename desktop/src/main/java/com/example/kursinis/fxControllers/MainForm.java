package com.example.kursinis.fxControllers;

import com.example.kursinis.hibernateControl.GenericHibernate;
import com.example.kursinis.model.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.io.IOException;

public class MainForm {

    @FXML
    public TableColumn<Cuisine, String> colCuisineDescription;
    @FXML
    public Button btnDeleteRating;

    @FXML private Tab tabRatings;
    // -------- UŽSAKYMAI --------
    @FXML
    private TableView<FoodOrder> orderTable;
    @FXML
    private TableColumn<FoodOrder, Number> colOrderId;
    @FXML
    private TableColumn<FoodOrder, String> colOrderName;
    @FXML
    private TableColumn<FoodOrder, Number> colOrderPrice;
    @FXML
    private TableColumn<FoodOrder, String> colOrderBuyer;
    @FXML
    private TableColumn<FoodOrder, String> colOrderRestaurant;
    @FXML
    private TableColumn<FoodOrder, String> colOrderDriver;
    @FXML
    private TableColumn<FoodOrder, String> colOrderStatus;

    @FXML private TableView<Rating> ratingTable;

    @FXML private TableColumn<Rating, Integer> colRatingId;
    @FXML private TableColumn<Rating, String> colRatingComment;
    @FXML private TableColumn<Rating, String> colRatingDate;
    @FXML private TableColumn<Rating, Integer> colRatingStars;
    @FXML private TableColumn<Rating, Integer> colRatingAuthor;
    @FXML private TableColumn<Rating, Integer> colRatingDriver;
    @FXML private TableColumn<Rating, Integer> colRatingRestaurant;


    @FXML private TextField txtFilterUserLogin;
    @FXML private TextField txtFilterUserName;
    @FXML private TextField txtFilterUserSurname;
    @FXML private TextField txtFilterUserPhone;

    @FXML private TextField txtFilterOrderName;
    @FXML private TextField txtFilterOrderBuyer;
    @FXML private ComboBox<OrderStatus> cmbFilterOrderStatus;


    // -------- VARTOTOJAI --------
    @FXML
    private TableView<BasicUser> userTable;
    @FXML
    private TableColumn<BasicUser, Number> colUserId;
    @FXML
    private TableColumn<BasicUser, String> colUserLogin;
    @FXML
    private TableColumn<BasicUser, String> colUserName;
    @FXML
    private TableColumn<BasicUser, String> colUserPhone;

    @FXML private Button btnAddRestaurant;
    @FXML private Button btnEditRestaurant;
    @FXML private Button btnDeleteRestaurant;

    // -------- RESTORANAI --------
    @FXML
    private TableView<Restaurant> restaurantTable;
    @FXML
    private TableColumn<Restaurant, String> colRestName;
    @FXML
    private TableColumn<Restaurant, String> colRestAddress;
    @FXML
    private TableColumn<Restaurant, String> colRestPhone;

    // -------- VAIRUOTOJAI --------
    @FXML
    private TableView<Driver> driverTable;
    @FXML
    private TableColumn<Driver, Number> colDriverId;
    @FXML
    private TableColumn<Driver, String> colDriverLogin;
    @FXML
    private TableColumn<Driver, String> colDriverName;
    @FXML
    private TableColumn<Driver, String> colDriverPhone;
    @FXML
    private TableColumn<Driver, String> colDriverVehicle;
    @FXML
    private TableColumn<Driver, String> colDriverLicence;

    @FXML private Button btnAddDriver;
    @FXML private Button btnEditDriver;
    @FXML private Button btnDeleteDriver;

    @FXML private Tab tabUsers;
    @FXML private Button btnAddUser;
    @FXML private Button btnEditUser;
    @FXML private Button btnDeleteUser;
    @FXML private Tab tabRestaurants;
    @FXML private Tab tabDrivers;
    @FXML private Tab tabCuisines;
    // -------- Cuisine --------
    @FXML
    private TableView<Cuisine> cuisineTable;
    @FXML
    private TableColumn<Cuisine, String> colCuisineName;
    @FXML
    private TableColumn<Cuisine, Number> colCuisinePrice;
    @FXML
    private TableColumn<Cuisine, String> colCuisineRestaurant;
    @FXML
    private TableColumn<Cuisine, String> colCuisineSpicy;
    @FXML
    private TableColumn<Cuisine, String> colCuisineVegan;

    @FXML private Button btnAddCuisine;
    @FXML private Button btnEditCuisine;
    @FXML private Button btnDeleteCuisine;
    // -------- Reviews --------
    @FXML private TableView<Review> reviewTable;
    @FXML private TableColumn<Review, Number> colReviewId;
    @FXML private TableColumn<Review, String> colReviewFrom;
    @FXML private TableColumn<Review, String> colReviewTo;
    @FXML private TableColumn<Review, Number> colReviewRating;
    @FXML private TableColumn<Review, String> colReviewDate;
    @FXML private TableColumn<Review, String> colReviewText;

    @FXML private Button btnAddReview;
    @FXML private Button btnEditReview;
    @FXML private Button btnDeleteReview;



    private final ObservableList<FoodOrder> orders      = FXCollections.observableArrayList();
    private final ObservableList<BasicUser> users       = FXCollections.observableArrayList();
    private final ObservableList<Restaurant> restaurants= FXCollections.observableArrayList();
    private final ObservableList<Driver> drivers        = FXCollections.observableArrayList();
    private final ObservableList<Cuisine> cuisines = FXCollections.observableArrayList();
    private final ObservableList<Review> reviews = FXCollections.observableArrayList();


    private final GenericHibernate gh = new GenericHibernate();

    private BasicUser loggedInUser;

    // -------- INIT --------
    @FXML
    public void initialize() {
        // Užsakymai
        colOrderId.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()));
        colOrderName.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        colOrderPrice.setCellValueFactory(data ->
                new javafx.beans.property.SimpleDoubleProperty(data.getValue().getPrice()));
        colOrderBuyer.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getBuyer() != null
                                ? data.getValue().getBuyer().getName()
                                : ""
                ));
        colOrderRestaurant.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getRestaurant() != null
                                ? data.getValue().getRestaurant().getName()
                                : ""
                ));
        colOrderDriver.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getDriver() != null
                                ? data.getValue().getDriver().getName()
                                : ""
                ));
        colOrderStatus.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getStatus() != null
                                ? data.getValue().getStatus().name()
                                : ""
                ));

        orderTable.setItems(orders);

        cmbFilterOrderStatus.setItems(FXCollections.observableArrayList(OrderStatus.values()));



        // Vartotojai
        colUserId.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()));
        colUserLogin.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getLogin()));
        colUserName.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        colUserPhone.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getPhoneNumber()));
        userTable.setItems(users);

        setupUserFilters();


        // Restoranai
        colRestName.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        colRestAddress.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getAddress()));
        colRestPhone.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getPhoneNumber()));
        restaurantTable.setItems(restaurants);

        // Vairuotojai
        if (driverTable != null) {
            colDriverId.setCellValueFactory(data ->
                    new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()));
            colDriverLogin.setCellValueFactory(data ->
                    new javafx.beans.property.SimpleStringProperty(data.getValue().getLogin()));
            colDriverName.setCellValueFactory(data ->
                    new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
            colDriverPhone.setCellValueFactory(data ->
                    new javafx.beans.property.SimpleStringProperty(data.getValue().getPhoneNumber()));
            colDriverVehicle.setCellValueFactory(data -> {
                VehicleType vt = data.getValue().getVehicleType();
                return new javafx.beans.property.SimpleStringProperty(vt != null ? vt.name() : "");
            });
            colDriverLicence.setCellValueFactory(data ->
                    new javafx.beans.property.SimpleStringProperty(
                            data.getValue().getLicence() != null ? data.getValue().getLicence() : ""
                    ));
            driverTable.setItems(drivers);
            // Patiekalai
            colCuisineName.setCellValueFactory(data ->
                    new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
            colCuisinePrice.setCellValueFactory(data ->
                    new javafx.beans.property.SimpleDoubleProperty(
                            data.getValue().getPrice() != null ? data.getValue().getPrice() : 0
                    ));
            colCuisineRestaurant.setCellValueFactory(data ->
                    new javafx.beans.property.SimpleStringProperty(
                            data.getValue().getRestaurant() != null
                                    ? data.getValue().getRestaurant().getName()
                                    : ""
                    ));
            colCuisineSpicy.setCellValueFactory(data ->
                    new javafx.beans.property.SimpleStringProperty(
                            data.getValue().isSpicy() ? "Taip" : "Ne"
                    ));
            colCuisineVegan.setCellValueFactory(data ->
                    new javafx.beans.property.SimpleStringProperty(
                            data.getValue().isVegan() ? "Taip" : "Ne"
                    ));
            colCuisineDescription.setCellValueFactory(data ->
                    new javafx.beans.property.SimpleStringProperty(
                            data.getValue().getDescription() != null
                                    ? data.getValue().getDescription()
                                    : ""
                    ));


            cuisineTable.setItems(cuisines);

            // Atsiliepimai
            colReviewId.setCellValueFactory(data ->
                    new SimpleIntegerProperty(data.getValue().getId()));

            colReviewFrom.setCellValueFactory(data -> {
                BasicUser owner = data.getValue().getCommentOwner();
                if (owner == null) return new SimpleStringProperty("-");

                String type = owner.getUserType();
                String name;

                switch (type) {
                    case "restaurant":
                        name = (owner instanceof Restaurant)
                                ? ((Restaurant) owner).getName()
                                : owner.getLogin();
                        break;

                    case "driver":
                        name = "Vairuotojas " + owner.getLogin();
                        break;

                    case "client":
                        name = owner.getName() + " " + owner.getSurname();
                        break;

                    default:
                        name = owner.getLogin();
                }

                if (name == null || name.isBlank()) name = owner.getLogin();
                return new SimpleStringProperty(name);
            });

            colReviewTo.setCellValueFactory(data -> {
                BasicUser target = data.getValue().getFeedbackUser();
                if (target == null) return new SimpleStringProperty("—");

                String name;
                switch (target.getUserType()) {
                    case "restaurant":
                        name = (target instanceof Restaurant)
                                ? ((Restaurant) target).getName()
                                : target.getLogin();
                        break;
                    case "driver":
                        name = "Vairuotojas " + target.getLogin();
                        break;
                    case "client":
                        name = target.getName() + " " + target.getSurname();
                        break;
                    default:
                        name = target.getLogin();
                }

                if (name == null || name.isBlank()) name = target.getLogin();
                return new SimpleStringProperty(name);
            });


            colReviewRating.setCellValueFactory(data ->
                    new SimpleIntegerProperty(data.getValue().getRating()));

            colReviewDate.setCellValueFactory(data ->
                    new SimpleStringProperty(
                            data.getValue().getDateCreated() != null
                                    ? data.getValue().getDateCreated().toString()
                                    : ""
                    ));

            colReviewText.setCellValueFactory(data ->
                    new SimpleStringProperty(
                            data.getValue().getReviewText() != null
                                    ? data.getValue().getReviewText()
                                    : ""
                    ));

            reviewTable.setItems(reviews);

            txtFilterOrderName.textProperty().addListener((obs, o, n) -> filterOrders());
            txtFilterOrderBuyer.textProperty().addListener((obs, o, n) -> filterOrders());
            cmbFilterOrderStatus.valueProperty().addListener((obs, o, n) -> filterOrders());


        }
        colRatingId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colRatingComment.setCellValueFactory(new PropertyValueFactory<>("comment"));
        colRatingDate.setCellValueFactory(new PropertyValueFactory<>("dateCreated"));
        colRatingStars.setCellValueFactory(new PropertyValueFactory<>("stars"));

        colRatingAuthor.setCellValueFactory(c ->
                new SimpleObjectProperty<>(c.getValue().getAuthor() != null ?
                        c.getValue().getAuthor().getId() : null));

        colRatingDriver.setCellValueFactory(c ->
                new SimpleObjectProperty<>(c.getValue().getDriver() != null ?
                        c.getValue().getDriver().getId() : null));

        colRatingRestaurant.setCellValueFactory(c ->
                new SimpleObjectProperty<>(c.getValue().getRestaurant() != null ?
                        c.getValue().getRestaurant().getId() : null));

        ratingTable.setItems(FXCollections.observableArrayList());
        loadRatings();


        reloadOrdersFromDb();
        reloadUsersFromDb();
        reloadRestaurantsFromDb();
        reloadReviewsFromDb();
    }

    private void reloadOrdersFromDb() {

        if (loggedInUser == null) {
            orders.clear();
            return;
        }

        // ADMIN mato viską
        if (loggedInUser.isAdmin()) {
            orders.setAll(gh.getAllRecords(FoodOrder.class));
        }
        // RESTORANAS mato tik savo užsakymus
        else if (loggedInUser.getUserType().equals("restaurant")) {

            orders.setAll(
                    gh.getAllRecords(FoodOrder.class)
                            .stream()
                            .filter(o -> o.getRestaurant() != null &&
                                    o.getRestaurant().getId() == loggedInUser.getId())
                            .toList()
            );
        }
        else {
            orders.clear();
        }

        orderTable.refresh();
    }


    private void reloadUsersFromDb() {
        users.setAll(gh.getAllRecords(BasicUser.class));
        userTable.refresh();
    }

    private void reloadRestaurantsFromDb() {

        if (loggedInUser == null) {
            restaurants.clear();
            return;
        }

        // ADMIN mato viską
        if (loggedInUser.isAdmin()) {
            restaurants.setAll(gh.getAllRecords(Restaurant.class));
        }
        // RESTORANAS mato tik SAVE
        else if (loggedInUser.getUserType().equals("restaurant")) {
            restaurants.setAll(
                    gh.getAllRecords(Restaurant.class)
                            .stream()
                            .filter(r -> r.getId() == loggedInUser.getId())
                            .toList()
            );
        }
        else {
            restaurants.clear();
        }

        restaurantTable.refresh();
    }


    private void reloadDriversFromDb() {
        if (driverTable != null) {
            drivers.setAll(gh.getAllRecords(Driver.class));
            driverTable.refresh();
        }
    }

    private void reloadCuisinesFromDb() {

        if (loggedInUser == null) {
            cuisines.clear();
            return;
        }

        if (loggedInUser.isAdmin()) {
            cuisines.setAll(gh.getAllRecords(Cuisine.class));
        }
        else if (loggedInUser.getUserType().equals("restaurant")) {

            cuisines.setAll(
                    gh.getAllRecords(Cuisine.class)
                            .stream()
                            .filter(c -> c.getRestaurant() != null &&
                                    c.getRestaurant().getId() == loggedInUser.getId())
                            .toList()
            );
        }
        else {
            cuisines.clear();
        }

        cuisineTable.refresh();
    }

    private void reloadReviewsFromDb() {

        if (loggedInUser == null) {
            reviews.clear();
            return;
        }

        if (loggedInUser.isAdmin()) {
            reviews.setAll(gh.getAllRecords(Review.class));
            reviewTable.refresh();
            return;
        }

        // --- RESTORANAS ---
        if (loggedInUser.getUserType().equals("restaurant")) {

            int restId = loggedInUser.getId();

            var allOrders = gh.getAllRecords(FoodOrder.class);

            reviews.setAll(
                    gh.getAllRecords(Review.class)
                            .stream()
                            .filter(r -> {
                                if (r.getChat() == null) return false;

                                int chatId = r.getChat().getId();

                                // Surandam tą order, kuris turi šį chatId
                                return allOrders.stream().anyMatch(o ->
                                        o.getChat() != null &&
                                                o.getChat().getId() == chatId &&
                                                o.getRestaurant() != null &&
                                                o.getRestaurant().getId() == restId
                                );
                            })
                            .toList()
            );
        }

        else {
            reviews.clear();
        }

        reviewTable.refresh();
    }

    // -------- MENU --------

    @FXML
    private void onExit(ActionEvent e) {
        System.exit(0);
    }

    @FXML
    private void onAbout(ActionEvent e) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION,
                "Kursinis darbas. Maisto užsakymo sistema.",
                ButtonType.OK);
        alert.setHeaderText("Apie");
        alert.showAndWait();
    }

    @FXML
    private void onNewOrder() {
        onAddOrder();
    }

    // -------- ORDER --------

    @FXML
    private void onAddOrder() {
        openOrderForm(null);
    }

    @FXML
    private void onEditOrder() {
        FoodOrder selected = orderTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Pasirinkite užsakymą");
            return;
        }
        openOrderForm(selected);
    }

    @FXML
    private void onDeleteOrder() {
        if (!isAdmin()) {
            showError("Neturite teisės šalinti užsakymų");
            return;
        }

        FoodOrder selected = orderTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Pasirinkite užsakymą");
            return;
        }

        try {
            gh.delete(FoodOrder.class, selected.getId());
            reloadOrdersFromDb();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Nepavyko ištrinti");
        }
    }

    private void openOrderForm(FoodOrder order) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/kursinis/order-form.fxml"));
            Scene scene = new Scene(loader.load());

            OrderForm controller = loader.getController();
            controller.setData(order, users);

            Stage stage = new Stage();
            stage.setTitle(order == null ? "Naujas užsakymas" : "Redaguoti užsakymą");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();

            reloadOrdersFromDb();

        } catch (IOException e) {
            e.printStackTrace();
            showError("Nepavyko atidaryti");
        }
    }

    // -------- VARTOTOJAI --------

    @FXML
    private void onNewUser() {
        onAddUser();
    }

    @FXML
    private void onAddUser() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/kursinis/user-form.fxml"));
            Scene scene = new Scene(loader.load());
            UserForm controller = loader.getController();
            controller.setData(null, (user, isNew) -> {
                System.out.println("User saved: " + user.getLogin());
            });
            Stage stage = new Stage();
            stage.setTitle("Naujas vartotojas");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();

            reloadUsersFromDb();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onEditUser(ActionEvent actionEvent) {
        BasicUser selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Pasirinkite vartotoją");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/kursinis/user-form.fxml"));
            Scene scene = new Scene(loader.load());
            UserForm controller = loader.getController();

            controller.setData(selected, (user, isNew) -> {
                try {
                    if (isNew) {
                        gh.create(user);
                    } else {
                        gh.update(user);
                    }
                    reloadUsersFromDb();
                } catch (Exception e) {
                    e.printStackTrace();
                    showError("Nepavyko atnaujinti");
                }
            });

            Stage stage = new Stage();
            stage.setTitle("Redaguoti vartotoją");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Nepavyko atidaryti");
        }
    }

    @FXML
    public void onDeleteUser(ActionEvent actionEvent) {
        if (!isAdmin()) {
            showError("Neturite teisės šalinti vartotojų");
            return;
        }
        BasicUser selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Pasirinkite vartotoją");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Ar tikrai norite ištrinti vartotoją: " + selected.getLogin() + "?",
                ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText("Vartotojo šalinimas");
        var result = confirm.showAndWait();
        if (result.isEmpty() || result.get() == ButtonType.NO) {
            return;
        }

        try {
            gh.delete(BasicUser.class, selected.getId());
            reloadUsersFromDb();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Nepavyko ištrinti");
        }
    }
    private void applyPermissions() {
        if (loggedInUser == null) return;

        boolean admin = loggedInUser.isAdmin();
        boolean isRest = loggedInUser.getUserType().equals("restaurant");

        // --- Cuisines ---
        if (btnDeleteCuisine != null)
            btnDeleteCuisine.setDisable(!(admin || isRest));

        if (btnEditCuisine != null)
            btnEditCuisine.setDisable(!(admin || isRest));

        if (btnAddCuisine != null)
            btnAddCuisine.setDisable(!(admin || isRest));

        if (tabRatings != null)
            tabRatings.setDisable(!admin);

        if (tabUsers != null)
            tabUsers.setDisable(!admin);

        if (tabRestaurants != null)
            tabRestaurants.setDisable(!admin);

        boolean isRestr = loggedInUser.getUserType().equals("restaurant");

        if (btnAddRestaurant != null)
            btnAddRestaurant.setDisable(!admin);  // tik admin

        if (btnDeleteRestaurant != null)
            btnDeleteRestaurant.setDisable(!admin); // tik admin

        if (btnEditRestaurant != null)
            btnEditRestaurant.setDisable(!(admin || isRestr)); // admin + restoranas


        if (tabDrivers != null)
            tabDrivers.setDisable(!admin);

        if (tabCuisines != null)
            tabCuisines.setDisable(!(admin || isRest));
    }

    // -------- RESTORANAI --------

    @FXML
    public void onAddRestaurant(ActionEvent actionEvent) {
        openRestaurantForm(null);
    }

    @FXML
    public void onEditRestaurant(ActionEvent actionEvent) {
        Restaurant selected = restaurantTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Pasirinkite restoraną");
            return;
        }
        openRestaurantForm(selected);
    }

    @FXML
    public void onDeleteRestaurant(ActionEvent actionEvent) {
        if (!isAdmin()) {
            showError("Neturite teisės šalinti restoranų");
            return;
        }

        Restaurant selected = restaurantTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Pasirinkite restoraną");
            return;
        }
        try {
            gh.delete(Restaurant.class, selected.getId());
            reloadRestaurantsFromDb();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Nepavyko ištrinti");
        }
    }

    private void openRestaurantForm(Restaurant restaurant) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/kursinis/restaurant-form.fxml"));
            Scene scene = new Scene(loader.load());

            RestaurantForm controller = loader.getController();
            controller.setData(restaurant);

            Stage stage = new Stage();
            stage.setTitle(restaurant == null ? "Naujas restoranas" : "Redaguoti restoraną");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();

            reloadRestaurantsFromDb();

        } catch (IOException e) {
            e.printStackTrace();
            showError("Nepavyko atidaryti");
        }
    }

    // -------- VAIRUOTOJAI --------

    @FXML
    private void onAddDriver() {
        openDriverForm(null);
    }

    @FXML
    private void onEditDriver() {
        if (driverTable == null) return;
        Driver selected = driverTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Pasirinkite vairuotoją");
            return;
        }
        openDriverForm(selected);
    }

    @FXML
    private void onDeleteDriver() {
        if (!isAdmin()) {
            showError("Neturite teisės šalinti vairuotojų");
            return;
        }
        if (driverTable == null) return;
        Driver selected = driverTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Pasirinkite vairuotoją");
            return;
        }
        try {
            gh.delete(Driver.class, selected.getId());
            reloadDriversFromDb();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Nepavyko ištrinti");
        }
    }

    private void openDriverForm(Driver driver) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/kursinis/driver-form.fxml"));
            Scene scene = new Scene(loader.load());

            DriverForm controller = loader.getController();
            controller.setData(driver);

            Stage stage = new Stage();
            stage.setTitle(driver == null ? "Naujas vairuotojas" : "Redaguoti vairuotoją");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();

            reloadDriversFromDb();

        } catch (IOException e) {
            e.printStackTrace();
            showError("Nepavyko atidaryti");
        }
    }

    // -------- BENDRA --------

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Klaida");
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public void setLoggedInUser(BasicUser user) {
        this.loggedInUser = user;
        System.out.println("Prisijungęs vartotojas: " + user.getLogin());
        applyPermissions();
        reloadCuisinesFromDb();
        reloadOrdersFromDb();
        reloadReviewsFromDb();
        reloadDriversFromDb();
        reloadRestaurantsFromDb();
    }
    private boolean isAdmin() {
        return loggedInUser != null && loggedInUser.isAdmin();
    }

    @FXML
    private void onAddCuisine() {
        openCuisineForm(null);
    }

    @FXML
    private void onEditCuisine() {
        Cuisine selected = cuisineTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Pasirinkite patiekalą");
            return;
        }

        openCuisineForm(selected);
    }

    @FXML
    private void onDeleteCuisine() {

        Cuisine selected = cuisineTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Pasirinkite patiekalą");
            return;
        }

        try {
            gh.delete(Cuisine.class, selected.getId());
            reloadCuisinesFromDb();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Nepavyko ištrinti");
        }
    }

    private void openCuisineForm(Cuisine cuisine) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/kursinis/cuisine-form.fxml"));
            Scene scene = new Scene(loader.load());

            CuisineForm controller = loader.getController();

            controller.setData(cuisine, loggedInUser);

            Stage stage = new Stage();
            stage.setTitle(cuisine == null ? "Naujas patiekalas" : "Redaguoti patiekalą");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();

            reloadCuisinesFromDb();

        } catch (IOException e) {
            e.printStackTrace();
            showError("Nepavyko atidaryti");
        }
    }
    @FXML
    private void onOpenChat() {
        FoodOrder selected = orderTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Pasirinkite užsakymą");
            return;
        }

        if (loggedInUser.getUserType().equals("restaurant")) {
            if (selected.getRestaurant() == null ||
                    selected.getRestaurant().getId() != loggedInUser.getId()) {

                showError("Negalite atidaryti kito restorano užsakymo chat.");
                return;
            }
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/kursinis/chat-form.fxml"));
            Scene scene = new Scene(loader.load());

            ChatForm controller = loader.getController();
            controller.setData(selected, loggedInUser);

            Stage stage = new Stage();
            stage.setTitle("Chat su užsakymu #" + selected.getId());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Nepavyko atidaryti chat lango");
        }
    }

    @FXML
    private void onAddReview() {
        if (loggedInUser == null) {
            showError("Prisijunkite, kad galėtumėte rašyti atsil    iepimą");
            return;
        }
        openReviewForm(null);
    }

    @FXML
    private void onEditReview() {
        Review selected = reviewTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Pasirinkite atsiliepimą");
            return;
        }
        openReviewForm(selected);
    }

    @FXML
    private void onDeleteReview() {
        if (!isAdmin()) {
            showError("Neturite teisės šalinti atsiliepimų");
            return;
        }

        Review selected = reviewTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Pasirinkite atsiliepimą");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Ar tikrai norite ištrinti atsiliepimą?",
                ButtonType.YES, ButtonType.NO);
        var res = confirm.showAndWait();
        if (res.isEmpty() || res.get() == ButtonType.NO) return;

        try {
            gh.delete(Review.class, selected.getId());
            reloadReviewsFromDb();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Nepavyko ištrinti atsiliepimo");
        }
    }

    private void openReviewForm(Review review) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/kursinis/review-form.fxml"));
            Scene scene = new Scene(loader.load());

            ReviewForm controller = loader.getController();
            controller.setData(review, loggedInUser, users);

            Stage stage = new Stage();
            stage.setTitle(review == null ? "Naujas atsiliepimas" : "Redaguoti atsiliepimą");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();

            reloadReviewsFromDb();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Nepavyko atidaryti atsiliepimo formos");
        }
    }

    private void setupUserFilters() {
        txtFilterUserLogin.textProperty().addListener((obs, old, val) -> filterUsers());
        txtFilterUserName.textProperty().addListener((obs, old, val) -> filterUsers());
        txtFilterUserSurname.textProperty().addListener((obs, old, val) -> filterUsers());
        txtFilterUserPhone.textProperty().addListener((obs, old, val) -> filterUsers());
    }

    private void filterUsers() {
        String login = txtFilterUserLogin.getText().toLowerCase();
        String name = txtFilterUserName.getText().toLowerCase();
        String surname = txtFilterUserSurname.getText().toLowerCase();
        String phone = txtFilterUserPhone.getText().toLowerCase();

        users.setAll(
                gh.getAllRecords(BasicUser.class)
                        .stream()
                        .filter(u ->
                                safeLower(u.getLogin()).contains(login) &&
                                        safeLower(u.getName()).contains(name) &&
                                        safeLower(u.getSurname()).contains(surname) &&
                                        safeLower(u.getPhoneNumber()).contains(phone)
                        )
                        .toList()
        );
    }

    private String safeLower(String s) {
        return (s == null) ? "" : s.toLowerCase();
    }

    private void filterOrders() {
        String name = safeLower(txtFilterOrderName.getText());
        String buyer = safeLower(txtFilterOrderBuyer.getText());
        OrderStatus status = cmbFilterOrderStatus.getValue();

        orders.setAll(
                gh.getAllRecords(FoodOrder.class)
                        .stream()
                        .filter(o ->
                                safeLower(o.getName()).contains(name) &&
                                        safeLower(o.getBuyer() != null ? o.getBuyer().getName() : "")
                                                .contains(buyer) &&
                                        (status == null || o.getStatus() == status) &&
                                        (
                                                loggedInUser.isAdmin() ||
                                                        (o.getRestaurant() != null &&
                                                                o.getRestaurant().getId() == loggedInUser.getId())
                                        )
                        )
                        .toList()
        );

        orderTable.refresh();
    }

    private void loadRatings() {

        colRatingId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colRatingComment.setCellValueFactory(new PropertyValueFactory<>("comment"));
        colRatingDate.setCellValueFactory(new PropertyValueFactory<>("dateCreated"));
        colRatingStars.setCellValueFactory(new PropertyValueFactory<>("stars"));

        colRatingAuthor.setCellValueFactory(c ->
                new SimpleObjectProperty<>(
                        c.getValue().getAuthor() != null ? c.getValue().getAuthor().getId() : null
                )
        );

        colRatingDriver.setCellValueFactory(c ->
                new SimpleObjectProperty<>(
                        c.getValue().getDriver() != null ? c.getValue().getDriver().getId() : null
                )
        );

        colRatingRestaurant.setCellValueFactory(c ->
                new SimpleObjectProperty<>(
                        c.getValue().getRestaurant() != null ? c.getValue().getRestaurant().getId() : null
                )
        );

        ratingTable.getItems().setAll(gh.getAllRecords(Rating.class));
    }


    @FXML
    public void onDeleteRating(ActionEvent actionEvent) {
        Rating selected = ratingTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Pasirinkite įrašą");
            return;
        }

        if (!isAdmin()) {
            showError("Šalinti gali tik administratorius");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Ar tikrai norite ištrinti įvertinimą?",
                ButtonType.YES, ButtonType.NO);
        var res = confirm.showAndWait();
        if (res.isEmpty() || res.get() == ButtonType.NO)
            return;

        try {
            gh.delete(Rating.class, selected.getId());
            loadRatings();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Klaida šalinant įvertinimą");
        }
    }
}
