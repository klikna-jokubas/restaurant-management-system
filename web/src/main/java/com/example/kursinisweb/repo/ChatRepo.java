package com.example.kursinisweb.repo;

import com.example.kursinisweb.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepo extends JpaRepository<Chat, Integer> {
    Chat getChatByFoodOrderId(Integer foodOrderId);
}
