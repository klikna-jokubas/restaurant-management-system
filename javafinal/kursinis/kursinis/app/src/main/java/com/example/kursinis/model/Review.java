package com.example.kursinis.model;


public class Review {

    private int id;
    private String reviewText;
    private String dateCreated;
    private User commentOwner;

    public Review() {}

    @Override
    public String toString() {
        if (commentOwner == null)
            return reviewText;

        return commentOwner.getName() + ": " + reviewText;
    }

    public Review(int id, User commentOwner, String dateCreated, String reviewText) {
        this.id = id;
        this.commentOwner = commentOwner;
        this.dateCreated = dateCreated;
        this.reviewText = reviewText;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public User getCommentOwner() {
        return commentOwner;
    }

    public void setCommentOwner(User commentOwner) {
        this.commentOwner = commentOwner;
    }
}
