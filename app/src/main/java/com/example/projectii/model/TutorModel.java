package com.example.projectii.model;

public class TutorModel {
    int fees;
    String userId;
    String role;

    public float getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(float avgRating) {
        this.avgRating = avgRating;
    }

    float avgRating;
public TutorModel(){

}
    public TutorModel(int fees, String userId, String role, String fullName, String email, String address, String phone, String password, String qualification, String level, String teachingHour, String subjects, String khaltiNumber, String about, String mode, String imageUri, boolean isAvailable) {
        this.fees = fees;
        this.userId = userId;
        this.role = role;
        this.fullName = fullName;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.password = password;
        this.qualification = qualification;
        this.level = level;
        this.teachingHour = teachingHour;
        this.subjects = subjects;
        this.khaltiNumber = khaltiNumber;
        this.about = about;
        this.mode = mode;
        this.imageUri = imageUri;
        this.isAvailable = isAvailable;
    }

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
    boolean isAvailable;

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
