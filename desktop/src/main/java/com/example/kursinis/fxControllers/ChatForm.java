package com.example.kursinis.fxControllers;

import com.example.kursinis.hibernateControl.GenericHibernate;
import com.example.kursinis.model.BasicUser;
import com.example.kursinis.model.FoodOrder;
import com.example.kursinis.model.Restaurant;
import com.example.kursinis.model.Review;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;
import java.util.List;

public class ChatForm {

    @FXML private Label lblOrderInfo;
    @FXML private TextArea txtMessages;
    @FXML private TextArea txtInput;
    @FXML private Button btnSend;
    @FXML private Button btnDelete;

    private final GenericHibernate gh = new GenericHibernate();

    private BasicUser loggedUser;
    private FoodOrder order;
    private int chatId;

    public void setData(FoodOrder order, BasicUser user) {
        this.order = order;
        this.loggedUser = user;
        this.chatId = order.getChat().getId();

        lblOrderInfo.setText("Užsakymas #" + order.getId() + " (ChatID: " + chatId + ")");

        loadMessages();

        if (loggedUser.isAdmin()) {
            return;
        }

        if (loggedUser.getUserType().equals("restaurant")) {

            btnDelete.setDisable(true);

            if (order.getRestaurant() == null ||
                    order.getRestaurant().getId() != loggedUser.getId()) {

                txtInput.setDisable(true);
                btnSend.setDisable(true);
            }

            return;
        }
        btnDelete.setDisable(true);
    }


    private void loadMessages() {
        txtMessages.clear();

        List<Review> messages = gh.getChatMessages(chatId);

        for (Review r : messages) {

            BasicUser owner = r.getCommentOwner();
            String author;

            switch (owner.getUserType()) {
                case "restaurant":
                    // Restaurant extends BasicUser, turi Restaurant.name (pavadinimą)
                    author = (owner instanceof Restaurant)
                            ? ((Restaurant) owner).getName()
                            : owner.getLogin();
                    break;

                case "driver":
                    author = "Vairuotojas " + owner.getLogin();
                    break;

                case "client":
                    author = owner.getName() + " " + owner.getSurname();
                    break;

                default:
                    author = owner.getLogin();
            }

            if (author == null || author.isBlank()) author = owner.getLogin();

            txtMessages.appendText("[" + author + "] " + r.getReviewText() + "\n");
        }
    }



}
