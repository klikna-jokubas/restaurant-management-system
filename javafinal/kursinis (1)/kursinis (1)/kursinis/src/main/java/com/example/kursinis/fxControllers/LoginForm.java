package com.example.kursinis.fxControllers;

import com.example.kursinis.hibernateControl.GenericHibernate;
import com.example.kursinis.model.BasicUser;
import com.example.kursinis.util.PasswordUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginForm {

    @FXML
    private TextField txtLogin;

    @FXML
    private PasswordField txtPassword;

    @FXML private Label lblMessage;



    private final GenericHibernate gh = new GenericHibernate();

    @FXML
    private void onLogin() {
        lblMessage.setText(""); // Išvalo seną tekstą

        String login = txtLogin.getText();
        String pass = txtPassword.getText();

        if (login.isBlank() || pass.isBlank()) {
            lblMessage.setText("Įveskite prisijungimo duomenis");
            return;
        }

        BasicUser user = gh.getByField(BasicUser.class, "login", login);
        if (user == null) {
            lblMessage.setText("Neteisingas login");
            return;
        }

        if (!PasswordUtil.checkPassword(pass, user.getPassword())) {
            lblMessage.setText("Neteisingas slaptažodis");
            return;
        }

        openMainWindow(user);

        ((Stage) txtLogin.getScene().getWindow()).close();
    }


    private void openMainWindow(BasicUser loggedInUser) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/kursinis/Main-view.fxml"));
            Scene scene = new Scene(loader.load());

            MainForm controller = loader.getController();
            controller.setLoggedInUser(loggedInUser);

            Stage stage = new Stage();
            stage.setTitle("Maisto užsakymo sistema");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Nepavyko atidaryti pagrindinio lango").showAndWait();
        }
    }

    @FXML
    private void onOpenRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/kursinis/register-view.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Registracija");
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Nepavyko atidaryti registracijos lango").showAndWait();
        }
    }
}
