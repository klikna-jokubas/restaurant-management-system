package com.example.kursinis.fxControllers;

import com.example.kursinis.hibernateControl.GenericHibernate;
import com.example.kursinis.model.BasicUser;
import com.example.kursinis.model.Review;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class ReviewForm {

    @FXML private ComboBox<BasicUser> cmbTargetUser;
    @FXML private Spinner<Integer> spRating;
    @FXML private TextArea txtText;
    @FXML private Label lblError;

    private final GenericHibernate gh = new GenericHibernate();

    private Review editingReview;
    private BasicUser currentUser;

    public void setData(Review review,
                        BasicUser currentUser,
                        ObservableList<BasicUser> allUsers) {
        this.currentUser = currentUser;

        cmbTargetUser.setItems(allUsers);

        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 5);
        spRating.setValueFactory(valueFactory);

        if (review != null) {
            this.editingReview = review;
            cmbTargetUser.setValue(review.getFeedbackUser());
            spRating.getValueFactory().setValue(review.getRating());
            txtText.setText(review.getReviewText());
        } else {
            this.editingReview = new Review();
        }
    }

    @FXML
    private void onSave() {
        lblError.setText("");

        BasicUser target = cmbTargetUser.getValue();
        if (target == null) {
            lblError.setText("Pasirinkite kam rašomas atsiliepimas");
            return;
        }

        int rating = spRating.getValue();
        if (rating < 1 || rating > 5) {
            lblError.setText("Įvertinimas turi būti 1–5");
            return;
        }

        String text = txtText.getText();
        if (text == null || text.isBlank()) {
            lblError.setText("Atsiliepimo tekstas privalomas");
            return;
        }

        editingReview.setFeedbackUser(target);
        editingReview.setRating(rating);
        editingReview.setReviewText(text);

        if (editingReview.getCommentOwner() == null) {
            editingReview.setCommentOwner(currentUser);
        }

        if (editingReview.getDateCreated() == null) {
            editingReview.setDateCreated(LocalDate.now());
        }

        try {
            if (editingReview.getId() == 0) {
                gh.create(editingReview);
            } else {
                gh.update(editingReview);
            }

            new Alert(Alert.AlertType.INFORMATION,
                    "Atsiliepimas išsaugotas",
                    ButtonType.OK).showAndWait();

            txtText.getScene().getWindow().hide();

        } catch (Exception e) {
            e.printStackTrace();
            lblError.setText("Klaida saugant atsiliepimą į DB");
        }
    }

    @FXML
    private void onCancel() {
        txtText.getScene().getWindow().hide();
    }
}
