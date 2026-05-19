package com.example.kursinis.fxControllers;

import com.example.kursinis.hibernateControl.GenericHibernate;
import com.example.kursinis.model.BasicUser;
import com.example.kursinis.model.Driver;
import com.example.kursinis.model.Restaurant;
import com.example.kursinis.model.VehicleType;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import com.example.kursinis.util.PasswordUtil;


public class RegisterForm {

    @FXML private ComboBox<String> cmbUserType;

    @FXML private TextField txtLogin;
    @FXML private PasswordField txtPassword;
    @FXML private TextField txtName;
    @FXML private TextField txtSurname;
    @FXML private TextField txtPhone;
    @FXML private TextField txtAddress;

    @FXML private CheckBox chkIsAdmin;

    @FXML private TextField txtLicence;
    @FXML private DatePicker dpBirthDate;
    @FXML private ComboBox<VehicleType> cmbVehicleType;

    @FXML private HBox rowName;
    @FXML private HBox rowSurname;
    @FXML private TitledPane driverPane;
    @FXML private Label lblName;

    @FXML private Label lblError;

    private final GenericHibernate gh = new GenericHibernate();

    @FXML
    public void initialize() {
        cmbUserType.setItems(FXCollections.observableArrayList(
                "Vartotojas", "Vairuotojas", "Restoranas"
        ));
        cmbUserType.getSelectionModel().selectFirst();

        cmbVehicleType.setItems(FXCollections.observableArrayList(VehicleType.values()));

        cmbUserType.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> updateFormForUserType(newVal)
        );

        updateFormForUserType(cmbUserType.getValue());
    }

    private void updateFormForUserType(String type) {
        if (type == null) return;

        if (type.equals("Restoranas")) {
            lblName.setText("Restorano pavadinimas:");

            rowSurname.setVisible(false);
            rowSurname.setManaged(false);

            driverPane.setVisible(false);
            driverPane.setManaged(false);

        } else if (type.equals("Vairuotojas")) {
            lblName.setText("Vardas:");

            rowSurname.setVisible(true);
            rowSurname.setManaged(true);

            driverPane.setVisible(true);
            driverPane.setManaged(true);

        } else {
            lblName.setText("Vardas:");

            rowSurname.setVisible(true);
            rowSurname.setManaged(true);

            driverPane.setVisible(false);
            driverPane.setManaged(false);
        }
    }

    @FXML
    private void onRegister() {
        lblError.setText("");

        String type = cmbUserType.getValue();
        String login = txtLogin.getText();
        String pass = txtPassword.getText();

        if (login == null || login.isBlank() ||
                pass == null || pass.isBlank()) {
            lblError.setText("Login ir slaptažodis privalomi");
            return;
        }

        String name = txtName.getText();
        String surname = txtSurname.getText();
        String phone = txtPhone.getText();
        String address = txtAddress.getText();

        if (phone != null && !phone.isBlank()
                && !phone.matches("\\+?\\d{8,12}")) {
            lblError.setText("Neteisingas telefono formatas");
            return;
        }

        try {
            if (type.equals("Restoranas")) {
                if (name == null || name.isBlank()) {
                    lblError.setText("Restorano pavadinimas privalomas");
                    return;
                }

                Restaurant rest = new Restaurant();
                rest.setLogin(login);
                rest.setPassword(PasswordUtil.hashPassword(pass));
                rest.setName(name);
                rest.setSurname(null);
                rest.setPhoneNumber(phone);
                rest.setAddress(address);
                rest.setAdmin(chkIsAdmin.isSelected());

                gh.create(rest);

            } else if (type.equals("Vairuotojas")) {
                if (name == null || name.isBlank() ||
                        surname == null || surname.isBlank()) {
                    lblError.setText("Vardas ir pavardė privalomi");
                    return;
                }
                if (dpBirthDate.getValue() == null) {
                    lblError.setText("Pasirinkite gimimo datą");
                    return;
                }
                if (cmbVehicleType.getValue() == null) {
                    lblError.setText("Pasirinkite transporto tipą");
                    return;
                }

                Driver d = new Driver();
                d.setLogin(login);
                d.setPassword(PasswordUtil.hashPassword(pass));
                d.setName(name);
                d.setSurname(surname);
                d.setPhoneNumber(phone);
                d.setAddress(address);
                d.setBDate(dpBirthDate.getValue());
                d.setVehicleType(cmbVehicleType.getValue());
                d.setAdmin(chkIsAdmin.isSelected());

                gh.create(d);

            } else {
                if (name == null || name.isBlank() ||
                        surname == null || surname.isBlank()) {
                    lblError.setText("Vardas ir pavardė privalomi");
                    return;
                }

                BasicUser u = new BasicUser();
                u.setLogin(login);
                u.setPassword(PasswordUtil.hashPassword(pass));
                u.setName(name);
                u.setSurname(surname);
                u.setPhoneNumber(phone);
                u.setAddress(address);
                u.setAdmin(chkIsAdmin.isSelected());

                gh.create(u);
            }

            new Alert(Alert.AlertType.INFORMATION,
                    "Registracija sėkminga!",
                    ButtonType.OK).showAndWait();

            txtLogin.getScene().getWindow().hide();

        } catch (Exception e) {
            e.printStackTrace();
            lblError.setText("Klaida registruojant vartotoją");
        }
    }

    @FXML
    private void onCancel() {
        txtLogin.getScene().getWindow().hide();
    }
}
