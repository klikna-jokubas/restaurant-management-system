package com.example.kursinis.fxControllers;

import com.example.kursinis.model.BasicUser;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.example.kursinis.util.PasswordUtil;


import java.util.function.BiConsumer;

public class UserForm {

    @FXML private TextField txtLogin;
    @FXML private PasswordField txtPassword;
    @FXML private TextField txtName;
    @FXML private TextField txtSurname;
    @FXML private TextField txtPhone;
    @FXML private TextField txtAddress;
    @FXML private Label lblError;

    private BasicUser user;
    private BiConsumer<BasicUser, Boolean> onSaveCallback;



    public void setData(BasicUser user, BiConsumer<BasicUser, Boolean> onSaveCallback) {
        this.onSaveCallback = onSaveCallback;
        if (user != null) {
            this.user = user;
            txtLogin.setText(user.getLogin());
            txtPassword.setText("");
            txtName.setText(user.getName());
            txtSurname.setText(user.getSurname());
            txtPhone.setText(user.getPhoneNumber());
            txtAddress.setText(user.getAddress());
        } else {
            this.user = new BasicUser();
        }
    }

    @FXML
    private void onSave() {
        if (txtLogin.getText().isBlank() || txtPassword.getText().isBlank()) {
            lblError.setText("Login ir slaptažodis privalomi");
            return;
        }
        if (!txtPhone.getText().matches("\\+?\\d{8,12}")) {
            lblError.setText("Neteisingas telefono formatas");
            return;
        }

        user.setLogin(txtLogin.getText());
        user.setName(txtName.getText());
        user.setSurname(txtSurname.getText());
        user.setPhoneNumber(txtPhone.getText());
        user.setAddress(txtAddress.getText());

        String plain = txtPassword.getText();

        if (!PasswordUtil.isHashed(plain)) {
            user.setPassword(PasswordUtil.hashPassword(plain));
        } else {
            user.setPassword(plain);
        }

        Alert ok = new Alert(Alert.AlertType.INFORMATION, "Vartotojas išsaugotas", ButtonType.OK);
        ok.showAndWait();

        boolean isNew = user.getId() == 0;
        onSaveCallback.accept(user, isNew);

        txtLogin.getScene().getWindow().hide();
    }


    @FXML
    private void onCancel() {
        txtLogin.getScene().getWindow().hide();
    }
}
