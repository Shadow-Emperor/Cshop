package com.abhi.cshop.model;

public class comment_model {

    private String comment;
    private String date;
    private String username;

    public comment_model(String comment , String date , String username) {
        this.comment = comment;
        this.date = date;
        this.username = username;
    }

    public comment_model() {
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
