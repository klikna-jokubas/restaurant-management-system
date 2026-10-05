package com.example.kursinis.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;

    protected int rating;

    @Column(length = 1000)
    protected String reviewText;

    protected LocalDate dateCreated;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    protected Chat chat;

    @ManyToOne
    @JoinColumn(name = "comment_owner_id")
    protected BasicUser commentOwner;   // kas parašė

    @ManyToOne
    @JoinColumn(name = "feedback_user_id")
    protected BasicUser feedbackUser;   // apie ką (restoranas, vairuotojas, kitas vartotojas)
}
