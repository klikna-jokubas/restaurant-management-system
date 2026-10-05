package com.example.kursinisweb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "comment_owner_id")
    private BasicUser commentOwner;

    @ManyToOne
    @JoinColumn(name = "feedback_user_id")
    @JsonIgnore
    private BasicUser feedbackUser;

    public Review() {}

    public Review(String reviewText, BasicUser commentOwner, Chat chat) {
        this.reviewText = reviewText;
        this.commentOwner = commentOwner;
        this.chat = chat;
        this.dateCreated = LocalDate.now();
    }

    @Override
    public String toString() {
        String sender = (commentOwner != null && commentOwner.getName() != null)
                ? commentOwner.getName()
                : "Unknown";

        String text = (reviewText != null) ? reviewText : "";

        return sender + ": " + text;
    }
}
