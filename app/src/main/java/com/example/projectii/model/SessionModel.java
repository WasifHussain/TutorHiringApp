package com.example.projectii.model;

public class SessionModel {
    String start_date;
    String end_date;
    long total_fees;
    String duration;
    String learnerID;
    String tutorID;
    String tutorName;
    String sessionId;
    boolean completed;

    public boolean isTutor_completed() {
        return tutor_completed;
    }

    public void setTutor_completed(boolean tutor_completed) {
        this.tutor_completed = tutor_completed;
    }

    boolean tutor_completed;
    public String getStart_date() {
        return start_date;
    }
    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }
    public String getEnd_date() {
        return end_date;
    }
    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }
    public String getLearnerID() {
        return learnerID;
    }
    public void setLearnerID(String learnerID) {
        this.learnerID = learnerID;
    }
    public String getTutorID() {
        return tutorID;
    }
    public void setTutorID(String tutorID) {
        this.tutorID = tutorID;
    }
    public long getTotal_fees() {
        return total_fees;
    }
    public void setTotal_fees(long total_fees) {
        this.total_fees = total_fees;
    }
    public String getSessionId() {
        return sessionId;
    }
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    public String getDuration() {
        return duration;
    }
    public void setDuration(String duration) {
        this.duration = duration;
    }
    public String getTutorName() {
        return tutorName;
    }
    public void setTutorName(String tutorName) {
        this.tutorName = tutorName;
    }
    public boolean isCompleted() {
        return completed;
    }
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
