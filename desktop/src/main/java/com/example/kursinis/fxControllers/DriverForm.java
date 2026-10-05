package com.example.kursinis.fxControllers;

import com.example.kursinis.hibernateControl.GenericHibernate;
import com.example.kursinis.model.Driver;
import com.example.kursinis.model.VehicleType;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class DriverForm {

    @FXML private TextField txtLogin;
    @FXML private PasswordField txtPassword;
    @FXML private TextField txtName;
    @FXML private TextField txtSurname;
    @FXML private TextField txtPhone;
    @FXML private TextField txtAddress;
    @FXML private TextField txtLicence;
    @FXML private DatePicker dateBirth;
    @FXML private ComboBox<VehicleType> cmbVehicle;
    @FXML private Label lblError;

    private final GenericHibernate gh = new GenericHibernate();
    private Driver editingDriver;

    public void setData(Driver driver) {
        cmbVehicle.getItems().setAll(VehicleType.values());

        if (driver != null) {
            this.editingDriver = driver;
            txtLogin.setText(driver.getLogin());
            txtPassword.setText(driver.getPassword());
            txtName.setText(driver.getName());
            txtSurname.setText(driver.getSurname());
            txtPhone.setText(driver.getPhoneNumber());
            txtAddress.setText(driver.getAddress());
            txtLicence.setText(driver.getLicence());
            dateBirth.setValue(driver.getBDate());
            cmbVehicle.setValue(driver.getVehicleType());
        } else {
            this.editingDriver = new Driver();
        }
    }

    @FXML
    private void onSave() {
        lblError.setText("");

        if (txtLogin.getText().isBlank() || txtPassword.getText().isBlank()) {
            lblError.setText("Login ir slaptažodis privalomi");
            return;
        }

        if (!txtPhone.getText().isBlank() &&
                !txtPhone.getText().matches("\\+?\\d{8,12}")) {
            lblError.setText("Neteisingas telefono formatas");
            return;
        }

        if (cmbVehicle.getValue() == null) {
            lblError.setText("Pasirinkite transporto tipą");
            return;
        }

        editingDriver.setLogin(txtLogin.getText());
        editingDriver.setPassword(txtPassword.getText());
        editingDriver.setName(txtName.getText());
        editingDriver.setSurname(txtSurname.getText());
        editingDriver.setPhoneNumber(txtPhone.getText());
        editingDriver.setAddress(txtAddress.getText());
        editingDriver.setLicence(txtLicence.getText());

        LocalDate bd = dateBirth.getValue();
        editingDriver.setBDate(bd);

        editingDriver.setVehicleType(cmbVehicle.getValue());

        try {
            if (editingDriver.getId() == 0) {
                gh.create(editingDriver);
            } else {
                gh.update(editingDriver);
            }

            new Alert(Alert.AlertType.INFORMATION,
                    "Vairuotojas išsaugotas",
                    ButtonType.OK).showAndWait();

            txtLogin.getScene().getWindow().hide();

        } catch (Exception e) {
            e.printStackTrace();
            lblError.setText("Klaida saugant");
        }
    }

    @FXML
    private void onCancel() {
        txtLogin.getScene().getWindow().hide();
    }
}
