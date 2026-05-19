package com.example.kursinisweb.controllers;

import com.example.kursinisweb.model.*;
import com.example.kursinisweb.repo.BasicUserRepo;
import com.example.kursinisweb.repo.ChatRepo;
import com.example.kursinisweb.repo.FoodOrderRepo;
import com.example.kursinisweb.repo.ReviewRepo;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;

@RestController
public class ChatController {

    @Autowired
    private ChatRepo chatRepo;

    @Autowired
    private BasicUserRepo basicUserRepo;

    @Autowired
    private FoodOrderRepo foodOrderRepo;

    @Autowired
    private ReviewRepo reviewRepo;


    @PostMapping("sendMessage")
    public @ResponseBody String sendMessage(@RequestBody String info) {

        Gson gson = new Gson();
        Properties props = gson.fromJson(info, Properties.class);

        String messageText = props.getProperty("messageText");
        int userId = Integer.parseInt(props.getProperty("userId"));
        int orderId = Integer.parseInt(props.getProperty("orderId"));

        BasicUser sender = basicUserRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        FoodOrder order = foodOrderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() == OrderStatus.DELIVERED ||
                order.getStatus() == OrderStatus.COMPLETED) {
            return "CHAT_CLOSED";
        }
        Chat chat = order.getChat();
        if (chat == null) {
            chat = new Chat();
            chat.setFoodOrder(order);
            chat.setChatText("Chat for order " + orderId);
            chat.setDateCreated(LocalDateTime.now());

            order.setChat(chat);
            chatRepo.save(chat);
            foodOrderRepo.save(order);
        }

        Review review = new Review(messageText, sender, chat);
        review.setDateCreated(LocalDate.now());

        reviewRepo.save(review);

        return "success";
    }



    @GetMapping("chat/messages/{orderId}")
    public @ResponseBody List<Review> getMessages(@PathVariable int orderId) {
        Chat chat = chatRepo.getChatByFoodOrderId(orderId);
        if (chat == null) return List.of();
        return chat.getMessages();
    }

}
