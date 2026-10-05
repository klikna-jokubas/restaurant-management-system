package com.example.kursinis.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "chats")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;

    protected String name;

    @Column(length = 4000)
    protected String chatText;

    protected LocalDateTime dateCreated;

    @OneToOne
    @JoinColumn(name = "order_id")
    protected FoodOrder foodOrder;
}
