package com.abhi.cshop.model;

public class  user {
    String identification;
    String Username;

    public user() {
    }

    public user(String identification , String username) {
        this.identification = identification;
        this.Username = username;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getUsername() {
        return this.Username;
    }

    public void setUsername(String username) {
        this.Username = username;
    }
}