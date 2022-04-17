package com.example.projectii.model;

import com.google.firebase.Timestamp;

public class ReviewModel {
    String tutorName;
    String tutorId;
    String learnerName;
    String review;
    String createdAt;
    float rating;

    public String getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    public String getTutorName() {
        return tutorName;
    }
    public void setTutorName(String tutorName) {
        this.tutorName = tutorName;
    }
    public String getTutorId() {
        return tutorId;
    }
    public void setTutorId(String tutorId) {
        this.tutorId = tutorId;
    }
    public String getLearnerName() {
        return learnerName;
    }
    public void setLearnerName(String learnerName) {
        this.learnerName = learnerName;
    }
    public String getReview() {
        return review;
    }
    public void setReview(String review) {
        this.review = review;
    }
    public float getRating() {
        return rating;
    }
    public void setRating(float rating) {
        this.rating = rating;
    }
}
