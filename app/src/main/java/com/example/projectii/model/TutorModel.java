package com.example.projectii.model;

public class TutorModel {
    int fees;
    String userId;
    String role;

    public Double
    getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }

    Double avgRating;
    String fullName;
    String email;
    String address;
    String phone;
    String password;
    String qualification;
    String level;
    String teachingHour;
    String subjects;
    String khaltiNumber;
    String about;
    String mode;
    String imageUri;

    public String getProfilePicUri() {
        return profilePicUri;
    }

    public void setProfilePicUri(String profilePicUri) {
        this.profilePicUri = profilePicUri;
    }

    String profilePicUri;
    boolean isAvailable;

    public int getPos_review_count() {
        return pos_review_count;
    }

    public void setPos_review_count(int pos_review_count) {
        this.pos_review_count = pos_review_count;
    }

    public int getNeg_review_count() {
        return neg_review_count;
    }

    public void setNeg_review_count(int neg_review_count) {
        this.neg_review_count = neg_review_count;
    }

    int pos_review_count;
    int neg_review_count;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getFees() {
        return fees;
    }

    public void setFees(int fees) {
        this.fees = fees;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getTeachingHour() {
        return teachingHour;
    }

    public void setTeachingHour(String teachingHour) {
        this.teachingHour = teachingHour;
    }

    public String getSubjects() {
        return subjects;
    }

    public void setSubjects(String subjects) {
        this.subjects = subjects;
    }

    public String getKhaltiNumber() {
        return khaltiNumber;
    }

    public void setKhaltiNumber(String khaltiNumber) {
        this.khaltiNumber = khaltiNumber;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
